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
                    val propStr = condition.prop?.name
                    if (condition.op == WhereClauseBuilder.Operator.Within) {
                        when (condition.value) {
                            is List<*> -> {
                                val valueList = (condition.value as List<*>).map { valueToMySQL(it!!) }
                                val valueStr = valueList.joinToString(",")
                                conditionList.add("$propStr IN ($valueStr)")
                            }

                            else -> throw Error("Unsupported type for 'within' operator ${condition.value::class.simpleName}")
                        }
                    } else {
                        val opStr = when (condition.op) {
                            WhereClauseBuilder.Operator.Equals -> "="
                            WhereClauseBuilder.Operator.NotEquals -> "!="
                            WhereClauseBuilder.Operator.GreaterThan -> ">"
                            WhereClauseBuilder.Operator.GreaterThanOrEquals -> ">="
                            WhereClauseBuilder.Operator.LessThan -> "<"
                            WhereClauseBuilder.Operator.LessThanOrEquals -> "<="

                            else -> throw Error("Unknown operator ${condition.op}")
                        }

                        // Wrap strings in single quotes for SQL
                        // otherwise use raw value
                        val valueStr = valueToMySQL(condition.value)
                        conditionList.add("$propStr$opStr$valueStr")
                    }
                }

                val conditionsStr = conditionList.joinToString(" AND ")
                whereClause = " WHERE $conditionsStr"
            }
            return "SELECT $fieldSelection FROM ${kClass.simpleName}$whereClause"
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