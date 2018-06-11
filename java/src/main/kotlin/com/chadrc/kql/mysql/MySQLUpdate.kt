package com.chadrc.kql.mysql

import com.chadrc.kql.statements.*
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

class MySQLUpdate<T : Any, I : Any>(private val kClass: KClass<T>, inputClass: KClass<I>, init: UpdateBuilder<T, I>.() -> Unit) {
    private val update = Update(kClass, inputClass, init)
    private val _params = ArrayList<KProperty<*>>()
    private val _queryString: String

    init {
        val typeName = kClass.simpleName
        val setStrings = ArrayList<String>()
        for (change in update.changes) {
            var value: Any? = null
            val str = when (change) {
                is Assignment<*> -> {
                    value = change.value
                    "${change.prop.name}=${valueToMySQL(change.value)}"
                }
                is Unset<*> -> "${change.prop.name}=NULL"
                is MathOperation<*> -> {
                    value = change.value
                    makeMathOperation(change)
                }

                else -> throw Error("Unsupported change type ${change::class.simpleName}")
            }

            if (value != null && value is KProperty<*>) {
                _params.add(value)
            }

            setStrings.add(str)
        }

        val allSets = setStrings.joinToString(",")
        val whereClause = makeWhereConditionString(update.conditions, _params)
        _queryString = "UPDATE $typeName SET $allSets$whereClause"
    }

    val queryString get() = _queryString
    val params get() = _params.toList()

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