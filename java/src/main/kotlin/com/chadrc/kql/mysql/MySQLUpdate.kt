package com.chadrc.kql.mysql

import com.chadrc.kql.statements.Assignment
import com.chadrc.kql.statements.Unset
import com.chadrc.kql.statements.Update
import com.chadrc.kql.statements.UpdateBuilder
import kotlin.reflect.KClass

class MySQLUpdate<T : Any>(private val kClass: KClass<T>, init: UpdateBuilder<T>.(T) -> Unit) {
    private val update = Update(kClass, init)

    val queryString: String
        get() {
            val typeName = kClass.simpleName
            val setStrings = ArrayList<String>()
            for (change in update.changes) {
                val str = when (change) {
                    is Assignment<*> -> "${change.prop.name}=${valueToMySQL(change.value)}"
                    is Unset<*> -> "${change.prop.name}=NULL"

                    else -> throw Error("Unsupported change type ${change::class.simpleName}")
                }

                setStrings.add(str)
            }

            val allSets = setStrings.joinToString(",")
            val whereClause = makeWhereConditionString(update.conditions)
            return "UPDATE $typeName SET $allSets$whereClause"
        }
}

inline fun <reified T : Any> kqlMySQLUpdate(noinline init: UpdateBuilder<T>.(T) -> Unit) = MySQLUpdate(T::class, init)