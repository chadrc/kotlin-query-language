package kql.statements

import kql.clauses.WhereClauseBuilder
import kotlin.reflect.KClass

class CountBuilder<T : Any>(private val kClass: KClass<T>) {
    private var whereClauseBuilder: WhereClauseBuilder<T>? = null

    fun where(init: WhereClauseBuilder<T>.(it: T) -> Unit) {
        whereClauseBuilder = WhereClauseBuilder(kClass)
        val primary = kClass.constructors.find { it.parameters.isEmpty() }
        if (primary != null && primary.parameters.isEmpty()) {
            val it = primary.call()
            whereClauseBuilder?.init(it)
        }
    }
}