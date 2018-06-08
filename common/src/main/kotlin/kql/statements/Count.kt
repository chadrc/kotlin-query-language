package kql.statements

import kotlin.reflect.KClass

class Count<T : Any>(kClass: KClass<T>, init: CountBuilder<T>.() -> Unit) {
    private val countBuilder = CountBuilder(kClass)

    init {
        countBuilder.init()
    }
}
