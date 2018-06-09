package kql.statements

import kql.clauses.WhereClauseBuilder
import kql.utils.stubInstanceAction
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

class Assignment<T>(val prop: KProperty<T>, val value: T)

class UpdateBuilder<T : Any>(private val kClass: KClass<T>) {
    private var _whereClauseBuilder: WhereClauseBuilder<T>? = null

    private val _changes = ArrayList<Assignment<*>>()

    val conditions get() = _whereClauseBuilder?.conditions
    val changes get() = _changes

    fun where(init: WhereClauseBuilder<T>.(it: T) -> Unit) {
        _whereClauseBuilder = WhereClauseBuilder(kClass)
        kClass.stubInstanceAction { _whereClauseBuilder?.init(it) }
    }

    infix fun <T : Any> KProperty<T>.toValue(b: T) {
        _changes.add(Assignment(this, b))
    }
}