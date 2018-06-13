package com.chadrc.kql.mysql.executor

import com.chadrc.kql.mysql.MySQLCount
import com.chadrc.kql.mysql.MySQLInsert
import com.chadrc.kql.mysql.MySQLSelect
import com.chadrc.kql.mysql.MySQLUpdate
import com.chadrc.kql.statements.CountBuilder
import com.chadrc.kql.statements.InsertBuilder
import com.chadrc.kql.statements.SelectBuilder
import com.chadrc.kql.statements.UpdateBuilder
import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
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

    fun select(init: SelectBuilder<T, Any>.() -> Unit): ResultSet? {
        val select = MySQLSelect(kClass, Any::class, init)
        val kqlStatement = conn?.createStatement()
        return kqlStatement?.executeQuery(select.queryString)
    }

    fun <I : Any> prepareSelect(inputClass: KClass<I>, init: SelectBuilder<T, I>.() -> Unit): PreparedKQLStatement<I> {
        val select = MySQLSelect(kClass, inputClass, init)
        return PreparedKQLStatement(_conn, select)
    }

    fun count(init: CountBuilder<T, Any>.() -> Unit): ResultSet? {
        val select = MySQLCount(kClass, Any::class, init)
        val kqlStatement = conn?.createStatement()
        return kqlStatement?.executeQuery(select.queryString)
    }

    fun <I : Any> prepareCount(inputClass: KClass<I>, init: CountBuilder<T, I>.() -> Unit): PreparedKQLStatement<I> {
        val count = MySQLCount(kClass, inputClass, init)
        return PreparedKQLStatement(_conn, count)
    }

    fun update(init: UpdateBuilder<T, Any>.() -> Unit): Int? {
        val update = MySQLUpdate(kClass, Any::class, init)
        val kqlStatement = conn?.createStatement()
        return kqlStatement?.executeUpdate(update.queryString)
    }

    fun <I : Any> prepareUpdate(inputClass: KClass<I>, init: UpdateBuilder<T, I>.() -> Unit): PreparedKQLStatement<I> {
        val count = MySQLUpdate(kClass, inputClass, init)
        return PreparedKQLStatement(_conn, count)
    }
}