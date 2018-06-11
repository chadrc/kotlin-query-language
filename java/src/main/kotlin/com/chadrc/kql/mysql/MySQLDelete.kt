package com.chadrc.kql.mysql

import com.chadrc.kql.statements.Delete
import com.chadrc.kql.statements.DeleteBuilder
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

class MySQLDelete<T : Any, I : Any>(private val kClass: KClass<T>, private val inputClass: KClass<I>, init: DeleteBuilder<T, I>.() -> Unit) {
    private val delete = Delete(kClass, inputClass, init)
    private val _params = ArrayList<KProperty<*>>()

    val queryString: String
        get() {
            if (delete.conditions.isEmpty()
                    && !delete.deleteAll) {
                return ""
            }
            val typeName = kClass.simpleName
            val whereClause = makeWhereConditionString(delete.conditions, _params)
            return "DELETE FROM $typeName$whereClause"
        }
}

inline fun <reified T : Any, reified I : Any> kqlMySQLDelete(noinline init: DeleteBuilder<T, I>.() -> Unit) = MySQLDelete(T::class, I::class, init)