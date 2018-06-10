package com.chadrc.kql.statements

import com.chadrc.kql.clauses.FieldClauseBuilder
import com.chadrc.kql.clauses.SortClauseBuilder
import com.chadrc.kql.clauses.WhereClauseBuilder
import kotlin.reflect.KClass

class SelectBuilder<T : Any, I : Any>(private val kClass: KClass<T>, private val inputClass: KClass<I>) {
    private var _fieldClauseBuilder: FieldClauseBuilder? = null
    private var _whereClauseBuilder: WhereClauseBuilder<T, I>? = null
    private var _sortClauseBuilder: SortClauseBuilder? = null
    private var _limit: Int = -1
    private var _offset: Int = -1

    val fieldClause get() = _fieldClauseBuilder
    val whereClause get() = _whereClauseBuilder
    val sortClause get() = _sortClauseBuilder
    val limit get() = _limit
    val offset get() = _offset

    fun fields(init: FieldClauseBuilder.() -> Unit) {
        _fieldClauseBuilder = FieldClauseBuilder()
        _fieldClauseBuilder?.init()
    }

    fun where(init: WhereClauseBuilder<T, I>.() -> Unit) {
        _whereClauseBuilder = WhereClauseBuilder(kClass, inputClass)
        _whereClauseBuilder?.init()
    }

    fun sort(init: SortClauseBuilder.() -> Unit) {
        _sortClauseBuilder = SortClauseBuilder()
        _sortClauseBuilder?.init()
    }

    fun limit(amount: Int) {
        _limit = amount
    }

    fun offset(amount: Int) {
        _offset = amount
    }
}