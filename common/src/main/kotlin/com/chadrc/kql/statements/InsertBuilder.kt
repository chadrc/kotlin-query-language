package com.chadrc.kql.statements

import com.chadrc.kql.utils.stubInstanceAction
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

class ValuePair<T>(val prop: KProperty<T>, val value: T)

class ValuesBuilder {
    private val _valuePairs = ArrayList<ValuePair<*>>()

    val valuePairs get() = _valuePairs.toList()

    infix fun <T> KProperty<T>.eq(value: T) {
        _valuePairs.add(ValuePair(this, value))
    }
}

class InsertBuilder<T : Any>(private val kClass: KClass<T>) {
    private val _records = ArrayList<ValuesBuilder>()

    val records get() = _records

    fun values(init: ValuesBuilder.(it: T) -> Unit) {
        val record = ValuesBuilder()
        kClass.stubInstanceAction { record.init(it) }
        _records.add(record)
    }
}