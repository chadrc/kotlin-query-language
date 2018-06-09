package mysql

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

                val fields = select.fields
                for (f in fields) {
                    fieldSelection = "$fieldSelection${f.prop.name},"
                }

                // Remove trailing comma
                fieldSelection = fieldSelection.slice(0 until fieldSelection.length - 1)
            }
            return "SELECT $fieldSelection FROM ${kClass.simpleName}"
        }
}

inline fun <reified T : Any> kqlMySQLSelect(noinline init: SelectBuilder<T>.() -> Unit) = MySQLSelect(T::class, init)