package com.chadrc.kql.statements

import com.chadrc.kql.clauses.WhereClauseBuilder
import kotlin.reflect.KClass

class Delete<T : Any>(kClass: KClass<T>, init: DeleteBuilder<T>.() -> Unit) {
    private val deleteBuilder = DeleteBuilder(kClass)

    val conditions get() = deleteBuilder.conditions ?: listOf<WhereClauseBuilder.Condition>()
    val deleteAll get() = deleteBuilder.deleteAll

    init {
        deleteBuilder.init()
    }
}
