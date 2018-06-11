package com.chadrc.kql.mysql

import com.chadrc.kql.statements.Delete
import com.chadrc.kql.statements.DeleteBuilder
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

class MySQLDelete<T : Any, I : Any>(private val kClass: KClass<T>, private val inputClass: KClass<I>, init: DeleteBuilder<T, I>.() -> Unit) {
    private val delete = Delete(kClass, inputClass, init)
    private val _params = ArrayList<KProperty<*>>()
    private val _queryString: String

    init {
        _queryString = if (delete.conditions.isEmpty() && !delete.deleteAll) "" else {
            val typeName = kClass.simpleName
            val whereClause = makeWhereConditionString(delete.conditions, _params)
            "DELETE FROM $typeName$whereClause"
        }
    }

    val queryString get() = _queryString
    val params get() = _params.toList()
}

inline fun <reified T : Any, reified I : Any> kqlMySQLDelete(noinline init: DeleteBuilder<T, I>.() -> Unit) = MySQLDelete(T::class, I::class, init)