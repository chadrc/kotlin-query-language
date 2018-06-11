package com.chadrc.kql.mysql

import com.chadrc.kql.clauses.WhereClauseBuilder
import kotlin.reflect.KProperty

fun valueToMySQL(value: Any?): String {
    if (value == null) {
        return "NULL"
    }

    // Wrap strings in single quotes for SQL
    // otherwise use raw value
    return when (value) {
        is String -> "'$value'"
        is Number -> value.toString()
        is Boolean -> if (value == true) "TRUE" else "FALSE"
        is KProperty<*> -> "?"

        else -> value.toString()
    }
}

fun makeWhereConditionString(conditions: List<WhereClauseBuilder.Condition>, params: ArrayList<KProperty<*>>): String {
    if (conditions.isEmpty()) {
        return ""
    }
    val conditionList = ArrayList<String>()
    for (condition in conditions) {
        conditionList.add(makeConditionString(condition, params))
    }

    val conditionsStr = conditionList.joinToString(" AND ")
    return " WHERE $conditionsStr"
}

private fun makeConditionString(condition: WhereClauseBuilder.Condition, params: ArrayList<KProperty<*>>): String {
    val propStr = condition.prop?.name
    val value = condition.value
    val op = condition.op

    val conditionStr = if (op == WhereClauseBuilder.Operator.Within
            || op == WhereClauseBuilder.Operator.NotWithin) {
        var conditionStr = when (value) {
            is List<*> -> {
                val valueList = value.map { valueToMySQL(it!!) }
                val valueStr = valueList.joinToString(",")
                "$propStr IN ($valueStr)"
            }

            is ClosedRange<*> -> {
                val min = valueToMySQL(value.start)
                val max = valueToMySQL(value.endInclusive)
                "$propStr BETWEEN $min AND $max"
            }

            else -> throw Error("Unsupported type for 'within' operator ${value::class.simpleName}")
        }

        // negate condition if 'Not'
        if (op == WhereClauseBuilder.Operator.NotWithin) {
            conditionStr = "NOT $conditionStr"
        }

        conditionStr
    } else if (op == WhereClauseBuilder.Operator.All
            || op == WhereClauseBuilder.Operator.Any) {
        val subConditions = value as List<*>
        val conditionStrings = subConditions.map { makeConditionString(it as WhereClauseBuilder.Condition, params) }
        val sep = if (op == WhereClauseBuilder.Operator.All) " AND " else " OR "

        conditionStrings.joinToString(sep)
    } else {
        val opStr = when (op) {
            WhereClauseBuilder.Operator.Equals -> "="
            WhereClauseBuilder.Operator.NotEquals -> "!="
            WhereClauseBuilder.Operator.GreaterThan -> ">"
            WhereClauseBuilder.Operator.GreaterThanOrEquals -> ">="
            WhereClauseBuilder.Operator.LessThan -> "<"
            WhereClauseBuilder.Operator.LessThanOrEquals -> "<="
            WhereClauseBuilder.Operator.Matches -> " LIKE "

        // Shouldn't be able to reach this since other operators are handled above
            else -> throw Error("Unknown operator $op")
        }

        // Wrap strings in single quotes for SQL
        // otherwise use raw value
        val valueStr = valueToMySQL(value)

        "$propStr$opStr$valueStr"
    }

    if (value is KProperty<*>) {
        params.add(value)
    }

    // Wrap in parenthesis to isolate from other statements
    return "($conditionStr)"
}