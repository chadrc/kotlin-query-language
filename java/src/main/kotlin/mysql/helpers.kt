package mysql

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