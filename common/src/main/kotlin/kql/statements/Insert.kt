package kql.statements

import kotlin.reflect.KClass

class Insert<T : Any>(kClass: KClass<T>, init: InsertBuilder<T>.() -> Unit) {
    private val insertBuilder = InsertBuilder(kClass)

    val records = insertBuilder.records

    init {
        insertBuilder.init()
    }
}
