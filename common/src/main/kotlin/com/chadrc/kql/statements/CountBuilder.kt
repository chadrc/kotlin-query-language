package com.chadrc.kql.statements

import com.chadrc.kql.clauses.WhereClauseBuilder
import kotlin.reflect.KClass

class CountBuilder<T : Any>(private val kClass: KClass<T>) {
    private var _whereClauseBuilder: WhereClauseBuilder<T>? = null

    val conditions get() = _whereClauseBuilder?.conditions

    fun where(init: WhereClauseBuilder<T>.() -> Unit) {
        _whereClauseBuilder = WhereClauseBuilder(kClass)
        _whereClauseBuilder?.init()
    }
}