package com.chadrc.kql.statements

import com.chadrc.kql.utils.stubInstanceAction
import kotlin.reflect.KClass

class Update<T : Any>(kClass: KClass<T>, init: UpdateBuilder<T>.(it: T) -> Unit) {
    private val updateBuilder = UpdateBuilder(kClass)

    val conditions get() = updateBuilder.conditions?.toList() ?: listOf()
    val changes get() = updateBuilder.changes.toList()

    init {
        kClass.stubInstanceAction { updateBuilder.init(it) }
    }
}
