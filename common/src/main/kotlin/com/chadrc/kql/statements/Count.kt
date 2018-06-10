package com.chadrc.kql.statements

import kotlin.reflect.KClass

class Count<T : Any>(kClass: KClass<T>, init: CountBuilder<T>.() -> Unit) {
    private val countBuilder = CountBuilder(kClass)

    val conditions get() = countBuilder.conditions?.toList() ?: listOf()

    init {
        countBuilder.init()
    }
}
