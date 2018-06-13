package com.chadrc.kql.statements

import kotlin.reflect.KClass

class Count<T : Any, I : Any>(private val _kClass: KClass<T>, inputClass: KClass<I>, init: CountBuilder<T, I>.() -> Unit) {
    private val countBuilder = CountBuilder(kClass, inputClass)

    val conditions get() = countBuilder.conditions?.toList() ?: listOf()
    val kClass get() = _kClass

    init {
        countBuilder.init()
    }
}
