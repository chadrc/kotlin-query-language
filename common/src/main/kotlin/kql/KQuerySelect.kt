package kql

import kql.clauses.KQueryFieldProjectionBuilder
import kql.clauses.KQuerySortClauseBuilder
import kql.clauses.KQueryWhereClauseBuilder
import kotlin.reflect.KClass

class KQuerySelect<T : Any>(kClass: KClass<T>, init: KQuerySelectBuilder<T>.() -> Unit) {
    private val selectBuilder: KQuerySelectBuilder<T> = KQuerySelectBuilder(kClass)

    init {
        selectBuilder.init()
    }
}

class KQuerySelectBuilder<T : Any>(private val kClass: KClass<T>) {
    private var fieldProjectionBuilder: KQueryFieldProjectionBuilder<T>? = null
    private var whereClauseBuilder: KQueryWhereClauseBuilder<T>? = null
    private var sortClauseBuilder: KQuerySortClauseBuilder<T>? = null
    private var limit: Int = -1
    private var offset: Int = -1

    fun fields(init: KQueryFieldProjectionBuilder<T>.(it: T) -> Unit) {
        fieldProjectionBuilder = KQueryFieldProjectionBuilder()
        val primary = kClass.constructors.find { it.parameters.isEmpty() }
        if (primary != null && primary.parameters.isEmpty()) {
            val it = primary.call()
            fieldProjectionBuilder?.init(it)
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

    fun sort(init: KQuerySortClauseBuilder<T>.(it: T) -> Unit) {
        sortClauseBuilder = KQuerySortClauseBuilder()
        val primary = kClass.constructors.find { it.parameters.isEmpty() }
        if (primary != null && primary.parameters.isEmpty()) {
            val it = primary.call()
            sortClauseBuilder?.init(it)
        }
    }

    fun limit(amount: Int) {
        limit = amount
    }

    fun offset(amount: Int) {
        offset = amount
    }
}