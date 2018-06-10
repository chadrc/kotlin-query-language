package com.chadrc.kql.statements

import com.chadrc.kql.clauses.WhereClauseBuilder
import com.chadrc.kql.utils.stubInstanceAction
import kotlin.reflect.KClass

class DeleteBuilder<T : Any>(private val kClass: KClass<T>) {
    private var _whereClauseBuilder: WhereClauseBuilder<T>? = null
    private var _all: Boolean = false

    val conditions get() = _whereClauseBuilder?.conditions
    val deleteAll get() = _all

    fun where(init: WhereClauseBuilder<T>.(it: T) -> Unit) {
        _whereClauseBuilder = WhereClauseBuilder(kClass)
        kClass.stubInstanceAction { _whereClauseBuilder?.init(it) }
    }

    fun all() {
        _all = true
    }
}