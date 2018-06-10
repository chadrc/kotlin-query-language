package mysql

import kql.clauses.Direction
import kql.clauses.WhereClauseBuilder
import kql.statements.Select
import kql.statements.SelectBuilder
import kotlin.reflect.KClass

class MySQLSelect<T : Any>(private val kClass: KClass<T>, init: SelectBuilder<T>.() -> Unit) {
    private val select: Select<T> = Select(kClass, init)

    val queryString: String
        get() {
            var fieldSelection = ""
            for (f in select.fields) {
                fieldSelection = "$fieldSelection${f.prop.name},"
            }

            // Remove trailing comma
            fieldSelection = fieldSelection.slice(0 until fieldSelection.length - 1)

            val selectClause = "SELECT $fieldSelection FROM ${kClass.simpleName}"

            val whereClause = if (select.conditions.isNotEmpty()) {
                val conditionList = ArrayList<String>()
                for (condition in select.conditions) {
                    conditionList.add(makeConditionString(condition))
                }

                val conditionsStr = conditionList.joinToString(" AND ")
                " WHERE $conditionsStr"
            } else ""

            val sortClause = if (select.sorts.isNotEmpty()) {
                val sortList = ArrayList<String>()
                for (sort in select.sorts) {
                    val dir = if (sort.dir == Direction.Ascending) "ASC" else "DESC"
                    val propStr = sort.prop.name
                    sortList.add("$propStr $dir")
                }
                val sortStr = sortList.joinToString(",")
                " ORDER BY $sortStr"
            } else ""

            val limitClause = if (select.limit > 0) " LIMIT ${select.limit}" else ""

            val offsetClause = if (select.offset > -1) " OFFSET ${select.offset}" else ""

            return "$selectClause$whereClause$sortClause$limitClause$offsetClause"
        }

    private fun makeConditionString(condition: WhereClauseBuilder.Condition): String {
        val propStr = condition.prop?.name
        val conditionStr = if (condition.op == WhereClauseBuilder.Operator.Within
                || condition.op == WhereClauseBuilder.Operator.NotWithin) {
            val value = condition.value
            var conditionStr = when (value) {
                is List<*> -> {
                    val valueList = value.map { valueToMySQL(it!!) }
                    val valueStr = valueList.joinToString(",")
                    "$propStr IN ($valueStr)"
                }

                is ClosedRange<*> -> {
                    val min = valueToMySQL(value.start)
                    val max = valueToMySQL(value.endInclusive)
                    "$propStr BETWEEN $min AND $max"
                }

                else -> throw Error("Unsupported type for 'within' operator ${value::class.simpleName}")
            }

            // negate condition if 'Not'
            if (condition.op == WhereClauseBuilder.Operator.NotWithin) {
                conditionStr = "NOT $conditionStr"
            }

            conditionStr
        } else if (condition.op == WhereClauseBuilder.Operator.All
                || condition.op == WhereClauseBuilder.Operator.Any) {
            val subConditions = condition.value as List<*>
            val conditionStrings = subConditions.map { makeConditionString(it as WhereClauseBuilder.Condition) }
            val sep = if (condition.op == WhereClauseBuilder.Operator.All) " AND " else " OR "

            conditionStrings.joinToString(sep)
        } else {
            val opStr = when (condition.op) {
                WhereClauseBuilder.Operator.Equals -> "="
                WhereClauseBuilder.Operator.NotEquals -> "!="
                WhereClauseBuilder.Operator.GreaterThan -> ">"
                WhereClauseBuilder.Operator.GreaterThanOrEquals -> ">="
                WhereClauseBuilder.Operator.LessThan -> "<"
                WhereClauseBuilder.Operator.LessThanOrEquals -> "<="
                WhereClauseBuilder.Operator.Matches -> " LIKE "

            // Should be able to reach this since other operators are handled above
                else -> throw Error("Unknown operator ${condition.op}")
            }

            // Wrap strings in single quotes for SQL
            // otherwise use raw value
            val valueStr = valueToMySQL(condition.value)

            "$propStr$opStr$valueStr"
        }

        // Wrap in parenthesis to isolate from other statements
        return "($conditionStr)"
    }

    private fun valueToMySQL(value: Any): String {
        // Wrap strings in single quotes for SQL
        // otherwise use raw value
        return when (value) {
            is String -> "'$value'"
            is Number -> value.toString()
            is Boolean -> if (value == true) "TRUE" else "FALSE"

            else -> value.toString()
        }
    }
}

inline fun <reified T : Any> kqlMySQLSelect(noinline init: SelectBuilder<T>.() -> Unit) = MySQLSelect(T::class, init)