package com.chadrc.kql.mysql

import com.chadrc.kql.statements.Delete
import com.chadrc.kql.statements.DeleteBuilder
import kotlin.reflect.KClass

class MySQLDelete<T : Any>(private val kClass: KClass<T>, init: DeleteBuilder<T>.() -> Unit) {
    private val delete = Delete(kClass, init)

    val queryString: String
        get() {
            if (delete.conditions.isEmpty()
                    && !delete.deleteAll) {
                return ""
            }
            val typeName = kClass.simpleName
            val whereClause = makeWhereConditionString(delete.conditions)
            return "DELETE FROM $typeName$whereClause"
        }
}

inline fun <reified T : Any> kqlMySQLDelete(noinline init: DeleteBuilder<T>.() -> Unit) = MySQLDelete(T::class, init)