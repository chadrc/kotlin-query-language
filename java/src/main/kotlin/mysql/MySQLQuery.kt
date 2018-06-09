package mysql

import kql.statements.Select
import kql.statements.SelectBuilder
import kotlin.reflect.KClass

class MySQLSelect<T : Any>(private val kClass: KClass<T>, init: SelectBuilder<T>.() -> Unit) {
    val select: Select<T> = Select(kClass, init)

    val queryString get() = "SELECT * FROM ${kClass.simpleName}"
}

inline fun <reified T : Any> kqlMySQLSelect(noinline init: SelectBuilder<T>.() -> Unit) = MySQLSelect(T::class, init)