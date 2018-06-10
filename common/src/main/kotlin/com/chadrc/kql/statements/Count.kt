package com.chadrc.kql.statements

import kotlin.reflect.KClass

class Count<T : Any, I : Any>(kClass: KClass<T>, inputClass: KClass<I>, init: CountBuilder<T, I>.() -> Unit) {
    private val countBuilder = CountBuilder(kClass, inputClass)

    val conditions get() = countBuilder.conditions?.toList() ?: listOf()

    init {
        countBuilder.init()
    }
}
