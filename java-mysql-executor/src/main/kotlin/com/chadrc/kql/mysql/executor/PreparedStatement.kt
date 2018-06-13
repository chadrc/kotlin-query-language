package com.chadrc.kql.mysql.executor

import com.chadrc.kql.mysql.MySQLStatement
import com.chadrc.kql.mysql.setAny
import java.sql.PreparedStatement
import java.sql.ResultSet

class PreparedStatement<T>(
        private val preparedStatement: PreparedStatement,
        private val statement: MySQLStatement
) : PreparedStatement by preparedStatement {

    fun execute(input: T): Boolean? {
        setParams(input)
        return execute()
    }

    fun executeQuery(input: T): ResultSet? {
        setParams(input)
        return executeQuery()
    }

    fun executeUpdate(input: T): Int? {
        setParams(input)
        return executeUpdate()
    }

    private fun setParams(input: T) {
        for ((index, prop) in statement.params.withIndex()) {
            setAny(index + 1, prop.getter.call(input))
        }
    }
}