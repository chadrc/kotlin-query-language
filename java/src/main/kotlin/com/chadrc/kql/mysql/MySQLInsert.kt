package com.chadrc.kql.mysql

import com.chadrc.kql.statements.Insert
import com.chadrc.kql.statements.InsertBuilder
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

class MySQLInsert<T : Any, I : Any>(private val kClass: KClass<T>, inputClass: KClass<I>, init: InsertBuilder<T, I>.() -> Unit) {
    private val insert = Insert(kClass, inputClass, init)
    private val _params = ArrayList<KProperty<*>>()

    val queryString: String
        get() {
            val typeName = kClass.simpleName

            val propList = ArrayList<String>()
            val propToValMaps = ArrayList<HashMap<String, Any?>>()

            for (record in insert.records) {
                val propToValMap = HashMap<String, Any?>()
                for (valuePair in record.valuePairs) {
                    if (!propList.contains(valuePair.prop.name)) {
                        propList.add(valuePair.prop.name)
                    }
                    propToValMap[valuePair.prop.name] = valuePair.value
                }

                propToValMaps.add(propToValMap)
            }

            // Sort to ensure consistent order for all records
            propList.sort()

            val propString = propList.joinToString(",")

            val valueStrings = ArrayList<String>()
            for (map in propToValMaps) {
                val values = ArrayList<Any?>()
                for (prop in propList) {
                    val value = if (map.containsKey(prop)) map[prop] else null
                    values.add(valueToMySQL(value))
                    if (value is KProperty<*>) {
                        params.add(value)
                    }
                }
                val valueString = values.joinToString(",")
                valueStrings.add("($valueString)")
            }

            val allValues = valueStrings.joinToString(",")

            return "INSERT INTO $typeName($propString) VALUES$allValues"
        }

    val params get() = _params
}

inline fun <reified T : Any, reified I : Any> kqlMySQLInsert(noinline init: InsertBuilder<T, I>.() -> Unit) = MySQLInsert(T::class, I::class, init)