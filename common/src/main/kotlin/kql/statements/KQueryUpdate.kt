package kql.statements

import kql.clauses.MutationClauseBuilder
import kql.clauses.WhereClauseBuilder
import kotlin.reflect.KClass

class KQueryUpdate<T : Any>(kClass: KClass<T>, init: KQueryUpdateBuilder<T>.() -> Unit) {
    private val updateBuilder = KQueryUpdateBuilder(kClass)

    init {
        updateBuilder.init()
    }
}

class KQueryUpdateBuilder<T : Any>(private val kClass: KClass<T>) {
    private var mutationClauseBuilder: MutationClauseBuilder<T>? = null
    private var whereClauseBuilder: WhereClauseBuilder<T>? = null

    fun set(init: MutationClauseBuilder<T>.(it: T) -> Unit) {
        mutationClauseBuilder = MutationClauseBuilder()
        val primary = kClass.constructors.find { it.parameters.isEmpty() }
        if (primary != null && primary.parameters.isEmpty()) {
            val it = primary.call()
            mutationClauseBuilder?.init(it)
        }
    }

    fun where(init: WhereClauseBuilder<T>.(it: T) -> Unit) {
        whereClauseBuilder = WhereClauseBuilder(kClass)
        val primary = kClass.constructors.find { it.parameters.isEmpty() }
        if (primary != null && primary.parameters.isEmpty()) {
            val it = primary.call()
            whereClauseBuilder?.init(it)
        }
    }
}