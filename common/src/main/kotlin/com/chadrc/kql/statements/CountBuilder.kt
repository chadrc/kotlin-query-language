package com.chadrc.kql.statements

import com.chadrc.kql.clauses.WhereClauseBuilder
import kotlin.reflect.KClass

class CountBuilder<T : Any, I : Any>(private val kClass: KClass<T>, private val inputClass: KClass<I>) {
    private var _whereClauseBuilder: WhereClauseBuilder<T, I>? = null

    val conditions get() = _whereClauseBuilder?.conditions

    fun where(init: WhereClauseBuilder<T, I>.() -> Unit) {
        _whereClauseBuilder = WhereClauseBuilder(kClass, inputClass)
        _whereClauseBuilder?.init()
    }
}