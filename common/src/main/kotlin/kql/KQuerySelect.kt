package kql

import kql.clauses.FieldSelector
import kql.clauses.KQueryFieldProjectionBuilder
import kql.clauses.KQuerySortClauseBuilder
import kql.clauses.KQueryWhereClauseBuilder
import kql.utils.stubInstanceAction
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

class KQuerySelect<T : Any>(private val kClass: KClass<T>, init: KQuerySelectBuilder<T>.() -> Unit) {
    private val selectBuilder: KQuerySelectBuilder<T> = KQuerySelectBuilder(kClass)

    class Field(val prop: KProperty<*>, val subFields: List<Field>? = null)

    val fields: List<Field>
        get() {
            val allProperties = kClass.members.filterIsInstance<KProperty<*>>()
            val clause = selectBuilder.fieldClause ?: return allProperties.map { Field(it) }

            return getProperties(kClass, clause)
        }

    val conditions: List<KQueryWhereClauseBuilder.Condition> get() = selectBuilder.whereClause?.conditions ?: listOf()

    init {
        selectBuilder.init()
    }

    private fun <T : Any> getProperties(kClass: KClass<T>, selector: FieldSelector): List<Field> {
        val allProperties = kClass.members.filterIsInstance<KProperty<*>>()

        // First, determine which fields will be used
        val props = if (selector.excludedFields?.size ?: 0 > 0) {
            allProperties.filter { prop -> selector.excludedFields?.find { it.prop.name == prop.name } == null }
        } else {
            allProperties.filter { prop -> selector.includedFields?.find { it.prop.name == prop.name } != null }
        }

        // Transform fields, if the field included a sub object selected
        return props.map { prop ->
            val fieldProp = selector.includedFields?.find { it.prop.name == prop.name }
            if (fieldProp?.includedFields != null
                    || fieldProp?.excludedFields != null) {
                val classType = prop.returnType.classifier as? KClass<Any>
                if (classType != null) {
                    Field(prop, getProperties(classType, fieldProp))
                } else {
                    Field(prop)
                }
            } else {
                Field(prop)
            }
        }
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
        _whereClauseBuilder = KQueryWhereClauseBuilder(kClass)
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