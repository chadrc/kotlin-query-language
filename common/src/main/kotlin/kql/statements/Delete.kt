package kql.statements

import kql.clauses.WhereClauseBuilder
import kotlin.reflect.KClass

class Delete<T : Any>(kClass: KClass<T>, init: DeleteBuilder<T>.() -> Unit) {
    private val deleteBuilder = DeleteBuilder(kClass)

    init {
        deleteBuilder.init()
    }
}

class DeleteBuilder<T : Any>(private val kClass: KClass<T>) {
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