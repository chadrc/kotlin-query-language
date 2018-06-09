package kql.statements

import kotlin.reflect.KClass

class Delete<T : Any>(kClass: KClass<T>, init: DeleteBuilder<T>.() -> Unit) {
    private val deleteBuilder = DeleteBuilder(kClass)

    val conditions get() = deleteBuilder.conditions
    val deleteAll get() = deleteBuilder.deleteAll

    init {
        deleteBuilder.init()
    }
}
