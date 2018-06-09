package kql.statements

import kql.clauses.WhereClauseBuilder
import kql.utils.stubInstanceAction
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

interface Change

class Assignment<T>(val prop: KProperty<T>, val value: T) : Change

class Unset<T>(val prop: KProperty<T>) : Change

enum class Operation {
    Increment
}

class MathOperation<T : Number>(val prop: KProperty<T>, val op: Operation, val value: T) : Change

class UpdateBuilder<T : Any>(private val kClass: KClass<T>) {
    private var _whereClauseBuilder: WhereClauseBuilder<T>? = null

    private val _changes = ArrayList<Change>()

    val conditions get() = _whereClauseBuilder?.conditions
    val changes get() = _changes

    fun where(init: WhereClauseBuilder<T>.(it: T) -> Unit) {
        _whereClauseBuilder = WhereClauseBuilder(kClass)
        kClass.stubInstanceAction { _whereClauseBuilder?.init(it) }
    }

    infix fun <T> KProperty<T>.toValue(v: T) {
        _changes.add(Assignment(this, v))
    }

    operator fun <T> KProperty<T>.unaryMinus() {
        unset(this)
    }

    fun <T> unset(prop: KProperty<T>) {
        _changes.add(Unset(prop))
    }

    infix fun <T : Number> KProperty<T>.inc(n: T) {
        _changes.add(MathOperation(this, Operation.Increment, n))
    }

    operator fun <T : Number> KProperty<T>.plusAssign(n: T) {
        this inc n
    }
}