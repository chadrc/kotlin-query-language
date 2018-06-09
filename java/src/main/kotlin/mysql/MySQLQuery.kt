package mysql

import kql.clauses.WhereClauseBuilder
import kql.statements.Select
import kql.statements.SelectBuilder
import kotlin.reflect.KClass

class MySQLSelect<T : Any>(private val kClass: KClass<T>, init: SelectBuilder<T>.() -> Unit) {
    private val select: Select<T> = Select(kClass, init)

    val queryString: String
        get() {
            var fieldSelection = "*"
            if (select.fields.isNotEmpty()) {
                fieldSelection = ""

                for (f in select.fields) {
                    fieldSelection = "$fieldSelection${f.prop.name},"
                }

                // Remove trailing comma
                fieldSelection = fieldSelection.slice(0 until fieldSelection.length - 1)
            }

            var whereClause = ""
            if (select.conditions.isNotEmpty()) {
                val conditionList = ArrayList<String>()
                for (condition in select.conditions) {
                    conditionList.add(makeConditionString(condition))
                }

                val conditionsStr = conditionList.joinToString(" AND ")
                whereClause = " WHERE $conditionsStr"
            }
            return "SELECT $fieldSelection FROM ${kClass.simpleName}$whereClause"
        }

    private fun makeConditionString(condition: WhereClauseBuilder.Condition): String {
        val propStr = condition.prop?.name
        if (condition.op == WhereClauseBuilder.Operator.Within
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

            // Wrap in parenthesis to isolate from other statements
            return "($conditionStr)"
        } else if (condition.op == WhereClauseBuilder.Operator.All
                || condition.op == WhereClauseBuilder.Operator.Any) {
            val subConditions = condition.value as List<WhereClauseBuilder.Condition>
            val conditionStrs = subConditions.map { makeConditionString(it) }
            val sep = if (condition.op == WhereClauseBuilder.Operator.All) " AND " else " OR "
            val combined = conditionStrs.joinToString(sep)
            return "($combined)"
        } else {
            val opStr = when (condition.op) {
                WhereClauseBuilder.Operator.Equals -> "="
                WhereClauseBuilder.Operator.NotEquals -> "!="
                WhereClauseBuilder.Operator.GreaterThan -> ">"
                WhereClauseBuilder.Operator.GreaterThanOrEquals -> ">="
                WhereClauseBuilder.Operator.LessThan -> "<"
                WhereClauseBuilder.Operator.LessThanOrEquals -> "<="
                WhereClauseBuilder.Operator.Matches -> " LIKE "

                else -> throw Error("Unknown operator ${condition.op}")
            }

            // Wrap strings in single quotes for SQL
            // otherwise use raw value
            val valueStr = valueToMySQL(condition.value)
            return "($propStr$opStr$valueStr)"
        }
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