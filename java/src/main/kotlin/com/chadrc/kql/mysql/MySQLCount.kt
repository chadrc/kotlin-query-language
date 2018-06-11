package com.chadrc.kql.mysql

import com.chadrc.kql.statements.Count
import com.chadrc.kql.statements.CountBuilder
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

class MySQLCount<T : Any, I : Any>(private val kClass: KClass<T>, private val inputClass: KClass<I>, init: CountBuilder<T, I>.() -> Unit) {
    private val count = Count(kClass, inputClass, init)
    private val _params = ArrayList<KProperty<*>>()

    val queryString: String
        get() {
            val typeName = kClass.simpleName
            val whereClause = makeWhereConditionString(count.conditions, _params)
            return "SELECT COUNT(*) FROM $typeName$whereClause"
        }
}

inline fun <reified T : Any, reified I : Any> kqlMySQLCount(noinline init: CountBuilder<T, I>.() -> Unit) = MySQLCount(T::class, I::class, init)