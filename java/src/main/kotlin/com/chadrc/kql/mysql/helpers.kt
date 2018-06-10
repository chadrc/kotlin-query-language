package com.chadrc.kql.mysql

import com.chadrc.kql.clauses.WhereClauseBuilder

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

        else -> value.toString()
    }
}

fun makeWhereConditionString(conditions: List<WhereClauseBuilder.Condition>): String {
    if (conditions.isEmpty()) {
        return ""
    }
    val conditionList = ArrayList<String>()
    for (condition in conditions) {
        conditionList.add(makeConditionString(condition))
    }

    val conditionsStr = conditionList.joinToString(" AND ")
    return " WHERE $conditionsStr"
}

private fun makeConditionString(condition: WhereClauseBuilder.Condition): String {
    val propStr = condition.prop?.name
    val conditionStr = if (condition.op == WhereClauseBuilder.Operator.Within
            || condition.op == WhereClauseBuilder.Operator.NotWithin) {
        val value = condition.value
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
        if (condition.op == WhereClauseBuilder.Operator.NotWithin) {
            conditionStr = "NOT $conditionStr"
        }

        conditionStr
    } else if (condition.op == WhereClauseBuilder.Operator.All
            || condition.op == WhereClauseBuilder.Operator.Any) {
        val subConditions = condition.value as List<*>
        val conditionStrings = subConditions.map { makeConditionString(it as WhereClauseBuilder.Condition) }
        val sep = if (condition.op == WhereClauseBuilder.Operator.All) " AND " else " OR "

        conditionStrings.joinToString(sep)
    } else {
        val opStr = when (condition.op) {
            WhereClauseBuilder.Operator.Equals -> "="
            WhereClauseBuilder.Operator.NotEquals -> "!="
            WhereClauseBuilder.Operator.GreaterThan -> ">"
            WhereClauseBuilder.Operator.GreaterThanOrEquals -> ">="
            WhereClauseBuilder.Operator.LessThan -> "<"
            WhereClauseBuilder.Operator.LessThanOrEquals -> "<="
            WhereClauseBuilder.Operator.Matches -> " LIKE "

        // Shouldn't be able to reach this since other operators are handled above
            else -> throw Error("Unknown operator ${condition.op}")
        }

        // Wrap strings in single quotes for SQL
        // otherwise use raw value
        val valueStr = valueToMySQL(condition.value)

        "$propStr$opStr$valueStr"
    }

    // Wrap in parenthesis to isolate from other statements
    return "($conditionStr)"
}