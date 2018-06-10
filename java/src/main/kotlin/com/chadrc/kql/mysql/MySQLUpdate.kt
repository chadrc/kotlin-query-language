package com.chadrc.kql.mysql

import com.chadrc.kql.statements.*
import kotlin.reflect.KClass

class MySQLUpdate<T : Any, I : Any>(private val kClass: KClass<T>, inputClass: KClass<I>, init: UpdateBuilder<T, I>.() -> Unit) {
    private val update = Update(kClass, inputClass, init)

    val queryString: String
        get() {
            val typeName = kClass.simpleName
            val setStrings = ArrayList<String>()
            for (change in update.changes) {
                val str = when (change) {
                    is Assignment<*> -> "${change.prop.name}=${valueToMySQL(change.value)}"
                    is Unset<*> -> "${change.prop.name}=NULL"
                    is MathOperation<*> -> makeMathOperation(change)

                    else -> throw Error("Unsupported change type ${change::class.simpleName}")
                }

                setStrings.add(str)
            }

            val allSets = setStrings.joinToString(",")
            val whereClause = makeWhereConditionString(update.conditions)
            return "UPDATE $typeName SET $allSets$whereClause"
        }

    private fun makeMathOperation(operation: MathOperation<*>): String {
        val propName = operation.prop.name
        val valueStr = valueToMySQL(operation.value)
        val mathStr = when (operation.op) {
            Operation.Increment -> "$propName+$valueStr"
            Operation.Decrement -> "$propName-$valueStr"
            Operation.Multiply -> "$propName*$valueStr"
            Operation.Divide -> "$propName/$valueStr"
            Operation.Remainder -> "$propName%$valueStr"
        }

        return "$propName=($mathStr)"
    }
}

inline fun <reified T : Any, reified I : Any> kqlMySQLUpdate(noinline init: UpdateBuilder<T, I>.() -> Unit) = MySQLUpdate(T::class, I::class, init)