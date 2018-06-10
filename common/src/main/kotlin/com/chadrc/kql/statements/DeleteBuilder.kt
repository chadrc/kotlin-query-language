package com.chadrc.kql.statements

import com.chadrc.kql.clauses.WhereClauseBuilder
import kotlin.reflect.KClass

class DeleteBuilder<T : Any, I : Any>(private val kClass: KClass<T>, private val inputClass: KClass<I>) {
    private var _whereClauseBuilder: WhereClauseBuilder<T, I>? = null
    private var _all: Boolean = false

    val conditions get() = _whereClauseBuilder?.conditions
    val deleteAll get() = _all

    fun where(init: WhereClauseBuilder<T, I>.() -> Unit) {
        _whereClauseBuilder = WhereClauseBuilder(kClass, inputClass)
        _whereClauseBuilder?.init()
    }

    fun all() {
        _all = true
    }
}