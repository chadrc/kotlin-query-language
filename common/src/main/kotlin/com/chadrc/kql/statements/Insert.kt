package com.chadrc.kql.statements

import kotlin.reflect.KClass

class Insert<T : Any, I : Any>(kClass: KClass<T>, inputClass: KClass<I>, init: InsertBuilder<T, I>.() -> Unit) {
    private val insertBuilder = InsertBuilder(kClass, inputClass)

    val records = insertBuilder.records

    init {
        insertBuilder.init()
    }
}
