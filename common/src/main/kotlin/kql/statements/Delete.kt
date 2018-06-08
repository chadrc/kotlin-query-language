package kql.statements

import kotlin.reflect.KClass

class Delete<T : Any>(kClass: KClass<T>, init: DeleteBuilder<T>.() -> Unit) {
    private val deleteBuilder = DeleteBuilder(kClass)

    init {
        deleteBuilder.init()
    }
}
