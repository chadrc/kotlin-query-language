package com.chadrc.kql.mysql

import com.chadrc.kql.clauses.Direction
import com.chadrc.kql.statements.*
import kotlin.reflect.KProperty

class MySQLStatement {
    private val _queryString: String
    private val _params = ArrayList<KProperty<*>>()

    val queryString get() = _queryString
    val params get() = _params.toList()

    constructor(insert: Insert<*, *>) {
        val typeName = insert.kClass.simpleName

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
                    _params.add(value)
                }
            }
            val valueString = values.joinToString(",")
            valueStrings.add("($valueString)")
        }

        val allValues = valueStrings.joinToString(",")

        _queryString = "INSERT INTO $typeName($propString) VALUES$allValues"
    }

    constructor(select: Select<*, *>) {
        var fieldSelection = ""
        for (f in select.fields) {
            fieldSelection = "$fieldSelection${f.prop.name},"
        }

        // Remove trailing comma
        fieldSelection = fieldSelection.slice(0 until fieldSelection.length - 1)

        val selectClause = "SELECT $fieldSelection FROM ${select.kClass.simpleName}"

        val whereClause = makeWhereConditionString(select.conditions, _params)

        val sortClause = if (select.sorts.isNotEmpty()) {
            val sortList = ArrayList<String>()
            for (sort in select.sorts) {
                val dir = if (sort.dir == Direction.Ascending) "ASC" else "DESC"
                val propStr = sort.prop.name
                sortList.add("$propStr $dir")
            }
            val sortStr = sortList.joinToString(",")
            " ORDER BY $sortStr"
        } else ""

        val limitClause = if (select.limit > 0) " LIMIT ${select.limit}" else ""

        val offsetClause = if (select.offset > -1) " OFFSET ${select.offset}" else ""

        _queryString = "$selectClause$whereClause$sortClause$limitClause$offsetClause"
    }

    constructor(count: Count<*, *>) {
        val typeName = count.kClass.simpleName
        val whereClause = makeWhereConditionString(count.conditions, _params)
        _queryString = "SELECT COUNT(*) FROM $typeName$whereClause"
    }

    constructor(update: Update<*, *>) {
        val typeName = update.kClass.simpleName
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

                    val propName = change.prop.name
                    val valueStr = valueToMySQL(change.value)
                    val mathStr = when (change.op) {
                        Operation.Increment -> "$propName+$valueStr"
                        Operation.Decrement -> "$propName-$valueStr"
                        Operation.Multiply -> "$propName*$valueStr"
                        Operation.Divide -> "$propName/$valueStr"
                        Operation.Remainder -> "$propName%$valueStr"
                    }

                    "$propName=($mathStr)"
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

    constructor(delete: Delete<*, *>) {
        _queryString = if (delete.conditions.isEmpty() && !delete.deleteAll) "" else {
            val typeName = delete.kClass.simpleName
            val whereClause = makeWhereConditionString(delete.conditions, _params)
            "DELETE FROM $typeName$whereClause"
        }
    }
}