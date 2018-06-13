package com.chadrc.kql.mysql.executor

import com.chadrc.kql.mysql.setAny
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet

class PreparedKQLStatement<T>(conn: Connection, private val statement: MySQLPreparable) {
    private val preparedStatement: PreparedStatement = conn.prepareStatement(statement.queryString)

    fun execute(input: T): Int {
        setParams(input)
        preparedStatement.execute()
        return preparedStatement.updateCount
    }

    fun executeQuery(input: T): ResultSet? {
        setParams(input)
        return preparedStatement.executeQuery()
    }

    fun executeUpdate(input: T): Int? {
        setParams(input)
        return preparedStatement.executeUpdate()
    }

    private fun setParams(input: T) {
        for ((index, prop) in statement.params.withIndex()) {
            preparedStatement.setAny(index + 1, prop.getter.call(input))
        }
    }
}