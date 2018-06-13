package com.chadrc.kql.mysql.executor

import com.chadrc.kql.mysql.MySQLStatement
import com.chadrc.kql.statements.*
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
        val insert = MySQLStatement(Insert(kClass, Any::class, init))
        val kqlStatement = conn?.createStatement()
        kqlStatement?.execute(insert.queryString)
    }

    fun <I : Any> prepareInsert(inputClass: KClass<I>, init: InsertBuilder<T, I>.() -> Unit): PreparedStatement<I> {
        val insert = MySQLStatement(Insert(kClass, inputClass, init))
        return PreparedStatement(_conn.prepareStatement(insert.queryString), insert)
    }

    fun select(init: SelectBuilder<T, Any>.() -> Unit): ResultSet? {
        val select = MySQLStatement(Select(kClass, Any::class, init))
        val kqlStatement = conn?.createStatement()
        return kqlStatement?.executeQuery(select.queryString)
    }

    fun <I : Any> prepareSelect(inputClass: KClass<I>, init: SelectBuilder<T, I>.() -> Unit): PreparedStatement<I> {
        val select = MySQLStatement(Select(kClass, inputClass, init))
        return PreparedStatement(_conn.prepareStatement(select.queryString), select)
    }

    fun count(init: CountBuilder<T, Any>.() -> Unit): ResultSet? {
        val select = MySQLStatement(Count(kClass, Any::class, init))
        val kqlStatement = conn?.createStatement()
        return kqlStatement?.executeQuery(select.queryString)
    }

    fun <I : Any> prepareCount(inputClass: KClass<I>, init: CountBuilder<T, I>.() -> Unit): PreparedStatement<I> {
        val count = MySQLStatement(Count(kClass, inputClass, init))
        return PreparedStatement(_conn.prepareStatement(count.queryString), count)
    }

    fun update(init: UpdateBuilder<T, Any>.() -> Unit): Int? {
        val update = MySQLStatement(Update(kClass, Any::class, init))
        val kqlStatement = conn?.createStatement()
        return kqlStatement?.executeUpdate(update.queryString)
    }

    fun <I : Any> prepareUpdate(inputClass: KClass<I>, init: UpdateBuilder<T, I>.() -> Unit): PreparedStatement<I> {
        val update = MySQLStatement(Update(kClass, inputClass, init))
        return PreparedStatement(_conn.prepareStatement(update.queryString), update)
    }

    fun delete(init: DeleteBuilder<T, Any>.() -> Unit): Int? {
        val delete = MySQLStatement(Delete(kClass, Any::class, init))
        val kqlStatement = conn?.createStatement()
        return kqlStatement?.executeUpdate(delete.queryString)
    }

    fun <I : Any> prepareDelete(inputClass: KClass<I>, init: DeleteBuilder<T, I>.() -> Unit): PreparedStatement<I> {
        val delete = MySQLStatement(Delete(kClass, inputClass, init))
        return PreparedStatement(_conn.prepareStatement(delete.queryString), delete)
    }
}