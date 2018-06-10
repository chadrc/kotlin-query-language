package com.chadrc.kql.statements

import com.chadrc.kql.clauses.WhereClauseBuilder
import kotlin.reflect.KClass

class Delete<T : Any, I : Any>(kClass: KClass<T>, inputClass: KClass<I>, init: DeleteBuilder<T, I>.() -> Unit) {
    private val deleteBuilder = DeleteBuilder(kClass, inputClass)

    val conditions get() = deleteBuilder.conditions ?: listOf<WhereClauseBuilder.Condition>()
    val deleteAll get() = deleteBuilder.deleteAll

    init {
        deleteBuilder.init()
    }
}
