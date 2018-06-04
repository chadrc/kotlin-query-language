package kql

import kql.clauses.KQueryFieldProjectionBuilder
import kql.clauses.KQuerySortClauseBuilder
import kql.clauses.KQueryWhereClauseBuilder
import kql.utils.stubInstanceAction
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

class KQuerySelect<T : Any>(private val kClass: KClass<T>, init: KQuerySelectBuilder<T>.() -> Unit) {
    private val selectBuilder: KQuerySelectBuilder<T> = KQuerySelectBuilder(kClass)

    val fields: List<KProperty<*>>
        get() {
            val allProperties = kClass.members.filterIsInstance<KProperty<*>>()
            println(allProperties)
            val clause = selectBuilder.fieldClause ?: return allProperties

            return if (clause.excludedFields.size > 0) {
                allProperties.filter { prop -> clause.excludedFields.find { it.name == prop.name } == null }
            } else {
                allProperties.filter { prop -> clause.includedFields.find { it.name == prop.name } != null }
            }
        }

    init {
        selectBuilder.init()
    }
}

class KQuerySelectBuilder<T : Any>(private val kClass: KClass<T>) {
    private var _fieldProjectionBuilder: KQueryFieldProjectionBuilder<T>? = null
    private var _whereClauseBuilder: KQueryWhereClauseBuilder<T>? = null
    private var _sortClauseBuilder: KQuerySortClauseBuilder<T>? = null
    private var _limit: Int = -1
    private var _offset: Int = -1

    val fieldClause get() = _fieldProjectionBuilder
    val whereClause get() = _whereClauseBuilder
    val sortClause get() = _sortClauseBuilder
    val limit get() = _limit
    val offset get() = _offset

    fun fields(init: KQueryFieldProjectionBuilder<T>.(it: T) -> Unit) {
        _fieldProjectionBuilder = KQueryFieldProjectionBuilder()
        kClass.stubInstanceAction { _fieldProjectionBuilder?.init(it) }
    }

    fun where(init: KQueryWhereClauseBuilder<T>.(it: T) -> Unit) {
        _whereClauseBuilder = KQueryWhereClauseBuilder()
        kClass.stubInstanceAction { _whereClauseBuilder?.init(it) }
    }

    fun sort(init: KQuerySortClauseBuilder<T>.(it: T) -> Unit) {
        _sortClauseBuilder = KQuerySortClauseBuilder()
        kClass.stubInstanceAction { _sortClauseBuilder?.init(it) }
    }

    fun limit(amount: Int) {
        _limit = amount
    }

    fun offset(amount: Int) {
        _offset = amount
    }
}