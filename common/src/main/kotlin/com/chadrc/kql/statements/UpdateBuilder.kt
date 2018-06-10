package com.chadrc.kql.statements

import com.chadrc.kql.clauses.WhereClauseBuilder
import com.chadrc.kql.exceptions.LeftPropOperandNotOnQueryClass
import com.chadrc.kql.exceptions.RightPropOperandNotOnInputClass
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

interface Change<T> {
    val prop: KProperty<T>
}

class Assignment<T>(override val prop: KProperty<T>, val value: Any?) : Change<T>

class Unset<T>(override val prop: KProperty<T>) : Change<T>

enum class Operation {
    Increment,
    Decrement,
    Multiply,
    Divide,
    Remainder
}

class MathOperation<T>(override val prop: KProperty<T>, val op: Operation, val value: Any) : Change<T>

class UpdateBuilder<T : Any, I : Any>(private val kClass: KClass<T>, private val inputClass: KClass<I>) {
    private var _whereClauseBuilder: WhereClauseBuilder<T, I>? = null

    private val _changes = ArrayList<Change<*>>()

    val conditions get() = _whereClauseBuilder?.conditions
    val changes get() = _changes

    fun where(init: WhereClauseBuilder<T, I>.() -> Unit) {
        _whereClauseBuilder = WhereClauseBuilder(kClass, inputClass)
        _whereClauseBuilder?.init()
    }

    infix fun <T> KProperty<T>.toValue(v: T) {
        assertOnClass(this)
        _changes.add(Assignment(this, v))
    }

    infix fun <T> KProperty<T>.toValue(v: KProperty<T>) {
        assertOnClass(this)
        assertOnInputClass(v)
        _changes.add(Assignment(this, v))
    }

    operator fun <T> KProperty<T>.unaryMinus() {
        unset(this)
    }

    fun <T> unset(prop: KProperty<T>) {
        assertOnClass(prop)
        _changes.add(Unset(prop))
    }

    infix fun <T : Number> KProperty<T>.add(n: T) {
        assertOnClass(this)
        _changes.add(MathOperation(this, Operation.Increment, n))
    }

    infix fun <T : Number> KProperty<T>.add(n: KProperty<T>) {
        assertOnClass(this)
        assertOnInputClass(n)
        _changes.add(MathOperation(this, Operation.Increment, n))
    }

    operator fun <T : Number> KProperty<T>.plusAssign(n: T) {
        this add n
    }

    operator fun <T : Number> KProperty<T>.plusAssign(n: KProperty<T>) {
        this add n
    }

    infix fun <T : Number> KProperty<T>.sub(n: T) {
        assertOnClass(this)
        _changes.add(MathOperation(this, Operation.Decrement, n))
    }

    infix fun <T : Number> KProperty<T>.sub(n: KProperty<T>) {
        assertOnClass(this)
        assertOnInputClass(n)
        _changes.add(MathOperation(this, Operation.Decrement, n))
    }

    operator fun <T : Number> KProperty<T>.minusAssign(n: T) {
        this sub n
    }

    operator fun <T : Number> KProperty<T>.minusAssign(n: KProperty<T>) {
        this sub n
    }

    infix fun <T : Number> KProperty<T>.mul(n: T) {
        assertOnClass(this)
        _changes.add(MathOperation(this, Operation.Multiply, n))
    }

    infix fun <T : Number> KProperty<T>.mul(n: KProperty<T>) {
        assertOnClass(this)
        assertOnInputClass(n)
        _changes.add(MathOperation(this, Operation.Multiply, n))
    }

    operator fun <T : Number> KProperty<T>.timesAssign(n: T) {
        this mul n
    }

    operator fun <T : Number> KProperty<T>.timesAssign(n: KProperty<T>) {
        this mul n
    }

    infix fun <T : Number> KProperty<T>.div(n: T) {
        assertOnClass(this)
        _changes.add(MathOperation(this, Operation.Divide, n))
    }

    infix fun <T : Number> KProperty<T>.div(n: KProperty<T>) {
        assertOnClass(this)
        assertOnInputClass(n)
        _changes.add(MathOperation(this, Operation.Divide, n))
    }

    operator fun <T : Number> KProperty<T>.divAssign(n: T) {
        this div n
    }

    operator fun <T : Number> KProperty<T>.divAssign(n: KProperty<T>) {
        this div n
    }

    infix fun <T : Number> KProperty<T>.rem(n: T) {
        assertOnClass(this)
        _changes.add(MathOperation(this, Operation.Remainder, n))
    }

    infix fun <T : Number> KProperty<T>.rem(n: KProperty<T>) {
        assertOnClass(this)
        assertOnInputClass(n)
        _changes.add(MathOperation(this, Operation.Remainder, n))
    }

    operator fun <T : Number> KProperty<T>.remAssign(n: T) {
        this rem n
    }

    operator fun <T : Number> KProperty<T>.remAssign(n: KProperty<T>) {
        this rem n
    }

    private fun assertOnClass(prop: KProperty<*>) {
        if (!kClass.members.contains(prop)) throw LeftPropOperandNotOnQueryClass(prop, kClass)
    }

    private fun assertOnInputClass(prop: KProperty<*>) {
        if (!inputClass.members.contains(prop)) throw RightPropOperandNotOnInputClass(prop, inputClass)
    }
}