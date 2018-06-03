package kql

import kql.clauses.KQueryMutationClauseBuilder
import kql.clauses.KQueryWhereClauseBuilder
import kotlin.reflect.KClass

class KQueryUpdate<T : Any>(kClass: KClass<T>, init: KQueryUpdateBuilder<T>.() -> Unit) {
    private val updateBuilder = KQueryUpdateBuilder(kClass)

    init {
        updateBuilder.init()
    }
}

class KQueryUpdateBuilder<T : Any>(private val kClass: KClass<T>) {
    private var mutationClauseBuilder: KQueryMutationClauseBuilder<T>? = null
    private var whereClauseBuilder: KQueryWhereClauseBuilder<T>? = null

    fun set(init: KQueryMutationClauseBuilder<T>.(it: T) -> Unit) {
        mutationClauseBuilder = KQueryMutationClauseBuilder()
        val primary = kClass.constructors.find { it.parameters.isEmpty() }
        if (primary != null && primary.parameters.isEmpty()) {
            val it = primary.call()
            mutationClauseBuilder?.init(it)
        }
    }

    fun where(init: KQueryWhereClauseBuilder<T>.(it: T) -> Unit) {
        whereClauseBuilder = KQueryWhereClauseBuilder()
        val primary = kClass.constructors.find { it.parameters.isEmpty() }
        if (primary != null && primary.parameters.isEmpty()) {
            val it = primary.call()
            whereClauseBuilder?.init(it)
        }
    }
}