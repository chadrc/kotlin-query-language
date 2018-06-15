package com.chadrc.kql.statements

import com.chadrc.kql.exceptions.LeftPropOperandNotOnQueryClass
import com.chadrc.kql.exceptions.RightPropOperandNotOnInputClass
import com.chadrc.kql.utils.getMembers
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

class ValuePair<T>(val prop: KProperty<T>, val value: T)

class ValuesBuilder(private val kClass: KClass<*>, private val inputClass: KClass<*>) {
    private val _valuePairs = ArrayList<ValuePair<*>>()

    val valuePairs get() = _valuePairs.toList()

    infix fun <T> KProperty<T>.eq(value: T) {
        assertOnClass(this)
        _valuePairs.add(ValuePair(this, value))
    }

    infix fun <T> KProperty<T>.eq(prop: KProperty<T>) {
        assertOnClass(this)
        assertOnInputClass(prop)
        _valuePairs.add(ValuePair(this, prop))
    }

    private fun assertOnClass(prop: KProperty<*>) {
        if (!getMembers(kClass).contains(prop)) throw LeftPropOperandNotOnQueryClass(prop, kClass)
    }

    private fun assertOnInputClass(prop: KProperty<*>) {
        if (!getMembers(inputClass).contains(prop)) throw RightPropOperandNotOnInputClass(prop, inputClass)
    }
}

class InsertBuilder<T : Any, I : Any>(private val kClass: KClass<T>, private val inputClass: KClass<I>) {
    private val _records = ArrayList<ValuesBuilder>()

    val records get() = _records

    fun values(init: ValuesBuilder.() -> Unit) {
        val record = ValuesBuilder(kClass, inputClass)
        record.init()
        _records.add(record)
    }
}