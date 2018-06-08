package kql.statements

import kql.clauses.WhereClauseBuilder
import kql.utils.stubInstanceAction
import kotlin.reflect.KClass

class CountBuilder<T : Any>(private val kClass: KClass<T>) {
    private var _whereClauseBuilder: WhereClauseBuilder<T>? = null

    val conditions get() = _whereClauseBuilder?.conditions

    fun where(init: WhereClauseBuilder<T>.(it: T) -> Unit) {
        _whereClauseBuilder = WhereClauseBuilder(kClass)
        kClass.stubInstanceAction { _whereClauseBuilder?.init(it) }
    }
}