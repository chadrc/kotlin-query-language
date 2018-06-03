package kql

import kql.clauses.KQueryWhereClauseBuilder
import kotlin.reflect.KClass

class KQueryCount<T : Any>(kClass: KClass<T>, init: KQueryCountBuilder<T>.() -> Unit) {
    private val countBuilder = KQueryCountBuilder(kClass)

    init {
        countBuilder.init()
    }
}

class KQueryCountBuilder<T : Any>(private val kClass: KClass<T>) {
    private var whereClauseBuilder: KQueryWhereClauseBuilder<T>? = null

    fun where(init: KQueryWhereClauseBuilder<T>.(it: T) -> Unit) {
        whereClauseBuilder = KQueryWhereClauseBuilder()
        val primary = kClass.constructors.find { it.parameters.isEmpty() }
        if (primary != null && primary.parameters.isEmpty()) {
            val it = primary.call()
            whereClauseBuilder?.init(it)
        }
    }
}