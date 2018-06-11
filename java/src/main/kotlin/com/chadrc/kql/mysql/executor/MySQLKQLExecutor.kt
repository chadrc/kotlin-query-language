package com.chadrc.kql.mysql.executor

import com.chadrc.kql.mysql.MySQLInsert
import com.chadrc.kql.statements.InsertBuilder
import java.sql.Connection
import java.sql.DriverManager
import kotlin.reflect.KClass

class MySQLKQLExecutor<T : Any>(private val kClass: KClass<T>) {
    private val _conn = DriverManager.getConnection(
            "jdbc:mysql://localhost:3306/test?user=root&password=password"
    )

    val conn: Connection? get() = _conn

    fun insert(init: InsertBuilder<T, Any>.() -> Unit) {
        val insert = MySQLInsert(kClass, Any::class, init)
        val kqlStatement = conn?.createStatement()
        kqlStatement?.execute(insert.queryString)
    }

    fun <I : Any> prepareInsert(inputClass: KClass<I>, init: InsertBuilder<T, I>.() -> Unit): PreparedKQLStatement<I> {
        val insert = MySQLInsert(kClass, inputClass, init)
        return PreparedKQLStatement(_conn, insert)
    }
}