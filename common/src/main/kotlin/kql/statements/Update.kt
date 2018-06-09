package kql.statements

import kql.utils.stubInstanceAction
import kotlin.reflect.KClass

class Update<T : Any>(kClass: KClass<T>, init: UpdateBuilder<T>.(it: T) -> Unit) {
    private val updateBuilder = UpdateBuilder(kClass)

    val conditions get() = updateBuilder.conditions?.toList()
    val changes get() = updateBuilder.changes.toList()

    init {
        kClass.stubInstanceAction { updateBuilder.init(it) }
    }
}
