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
                    val opStr = when (condition.op) {
                        WhereClauseBuilder.Operator.Equals -> "="

                        else -> throw Error()
                    }
                    val valueStr = condition.value.toString()
                    conditionList.add("$propStr$opStr$valueStr")
                }

                val conditionsStr = conditionList.joinToString(" AND ")
                whereClause = " WHERE $conditionsStr"
            }
            return "SELECT $fieldSelection FROM ${kClass.simpleName}$whereClause"
        }
}

inline fun <reified T : Any> kqlMySQLSelect(noinline init: SelectBuilder<T>.() -> Unit) = MySQLSelect(T::class, init)