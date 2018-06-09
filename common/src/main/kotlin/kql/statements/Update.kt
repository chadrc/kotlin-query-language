package kql.statements

import kotlin.reflect.KClass

class Update<T : Any>(kClass: KClass<T>, init: UpdateBuilder<T>.() -> Unit) {
    private val updateBuilder = UpdateBuilder(kClass)

    val conditions get() = updateBuilder.conditions

    init {
        updateBuilder.init()
    }
}
