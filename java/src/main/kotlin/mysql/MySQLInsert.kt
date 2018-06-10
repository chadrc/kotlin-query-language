package mysql

import kql.statements.Insert
import kql.statements.InsertBuilder
import kotlin.reflect.KClass

class MySQLInsert<T : Any>(private val kClass: KClass<T>, init: InsertBuilder<T>.() -> Unit) {
    private val insert = Insert(kClass, init)

    val queryString: String
        get() {
            val typeName = kClass.simpleName

            val propList = ArrayList<String>()
            val propToValMaps = ArrayList<HashMap<String, Any?>>()

            for (record in insert.records) {
                val propToValMap = HashMap<String, Any?>()
                for (valuePair in record.valuePairs) {
                    propList.add(valuePair.prop.name)
                    propToValMap[valuePair.prop.name] = valuePair.value
                }

                propToValMaps.add(propToValMap)
            }

            propList.sort()

            val propString = propList.joinToString(",")

            val valueStrings = ArrayList<String>()
            for (map in propToValMaps) {
                val values = ArrayList<Any?>()
                for (prop in propList) {
                    val value = if (map.containsKey(prop)) map[prop] else null
                    values.add(valueToMySQL(value))
                }
                val valueString = values.joinToString(",")
                valueStrings.add("($valueString)")
            }

            val allValues = valueStrings.joinToString(",")

            return "INSERT INTO $typeName($propString) VALUES$allValues"
        }
}

inline fun <reified T : Any> kqlMySQLInsert(noinline init: InsertBuilder<T>.() -> Unit) = MySQLInsert(T::class, init)