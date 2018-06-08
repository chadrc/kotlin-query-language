package kql.statements

import kql.clauses.WhereClauseBuilder
import kotlin.reflect.KClass

class KQueryDelete<T : Any>(kClass: KClass<T>, init: KQueryDeleteBuilder<T>.() -> Unit) {
    private val deleteBuilder = KQueryDeleteBuilder(kClass)

    init {
        deleteBuilder.init()
    }
}

class KQueryDeleteBuilder<T : Any>(private val kClass: KClass<T>) {
    private var whereClauseBuilder: WhereClauseBuilder<T>? = null
    private var all: Boolean = false

    fun where(init: WhereClauseBuilder<T>.(it: T) -> Unit) {
        whereClauseBuilder = WhereClauseBuilder(kClass)
        val primary = kClass.constructors.find { it.parameters.isEmpty() }
        if (primary != null && primary.parameters.isEmpty()) {
            val it = primary.call()
            whereClauseBuilder?.init(it)
        }
    }

    fun all() {
        all = true
    }
}