package com.chadrc.kql.mysql

import com.chadrc.kql.statements.Count
import com.chadrc.kql.statements.CountBuilder
import kotlin.reflect.KClass

class MySQLCount<T : Any>(private val kClass: KClass<T>, init: CountBuilder<T>.() -> Unit) {
    private val count = Count(kClass, init)

    val queryString: String
        get() {
            val typeName = kClass.simpleName
            return "SELECT COUNT(*) FROM $typeName"
        }
}

inline fun <reified T : Any> kqlMySQLCount(noinline init: CountBuilder<T>.() -> Unit) = MySQLCount(T::class, init)