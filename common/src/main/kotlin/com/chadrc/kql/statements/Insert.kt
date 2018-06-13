package com.chadrc.kql.statements

import kotlin.reflect.KClass

class Insert<T : Any, I : Any>(private val _kClass: KClass<T>, inputClass: KClass<I>, init: InsertBuilder<T, I>.() -> Unit) {
    private val insertBuilder = InsertBuilder(_kClass, inputClass)

    val records = insertBuilder.records
    val kClass get() = _kClass

    init {
        insertBuilder.init()
    }
}
