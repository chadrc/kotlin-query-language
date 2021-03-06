package com.chadrc.kql.statements

import kotlin.reflect.KClass

class Update<T : Any, I : Any>(private val _kClass: KClass<T>, inputClass: KClass<I>, init: UpdateBuilder<T, I>.() -> Unit) {
    private val updateBuilder = UpdateBuilder(kClass, inputClass)

    val conditions get() = updateBuilder.conditions?.toList() ?: listOf()
    val changes get() = updateBuilder.changes.toList()
    val kClass get() = _kClass

    init {
        updateBuilder.init()
    }
}
