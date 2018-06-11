package com.chadrc.kql.mysql.executor

import com.chadrc.kql.mysql.setAny
import java.sql.Connection
import java.sql.PreparedStatement

class PreparedKQLStatement<T>(conn: Connection, private val statement: MySQLPreparable) {
    private val preparedStatement: PreparedStatement = conn.prepareStatement(statement.queryString)

    fun execute(input: T) {
        for ((index, prop) in statement.params.withIndex()) {
            preparedStatement.setAny(index + 1, prop.getter.call(input))
        }

        preparedStatement.execute()
    }
}