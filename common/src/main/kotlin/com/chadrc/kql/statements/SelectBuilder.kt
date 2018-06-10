package com.chadrc.kql.statements

import com.chadrc.kql.clauses.FieldClauseBuilder
import com.chadrc.kql.clauses.SortClauseBuilder
import com.chadrc.kql.clauses.WhereClauseBuilder
import com.chadrc.kql.utils.stubInstanceAction
import kotlin.reflect.KClass

class SelectBuilder<T : Any>(private val kClass: KClass<T>) {
    private var _fieldClauseBuilder: FieldClauseBuilder? = null
    private var _whereClauseBuilder: WhereClauseBuilder<T>? = null
    private var _sortClauseBuilder: SortClauseBuilder? = null
    private var _limit: Int = -1
    private var _offset: Int = -1

    val fieldClause get() = _fieldClauseBuilder
    val whereClause get() = _whereClauseBuilder
    val sortClause get() = _sortClauseBuilder
    val limit get() = _limit
    val offset get() = _offset

    fun fields(init: FieldClauseBuilder.(it: T) -> Unit) {
        _fieldClauseBuilder = FieldClauseBuilder()
        kClass.stubInstanceAction { _fieldClauseBuilder?.init(it) }
    }

    fun where(init: WhereClauseBuilder<T>.(it: T) -> Unit) {
        _whereClauseBuilder = WhereClauseBuilder(kClass)
        kClass.stubInstanceAction { _whereClauseBuilder?.init(it) }
    }

    fun sort(init: SortClauseBuilder.(it: T) -> Unit) {
        _sortClauseBuilder = SortClauseBuilder()
        kClass.stubInstanceAction { _sortClauseBuilder?.init(it) }
    }

    fun limit(amount: Int) {
        _limit = amount
    }

    fun offset(amount: Int) {
        _offset = amount
    }
}