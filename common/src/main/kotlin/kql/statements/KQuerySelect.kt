package kql.statements

import kql.clauses.*
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

    val conditions: List<WhereClauseBuilder.Condition> get() = selectBuilder.whereClause?.conditions ?: listOf()

    val sorts: List<Sort> get() = selectBuilder.sortClause?.sorts ?: listOf()

    val limit get() = selectBuilder.limit

    val offset get() = selectBuilder.offset

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
                @Suppress("UNCHECKED_CAST")
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
    private var _fieldProjectionBuilder: FieldProjectionBuilder? = null
    private var _whereClauseBuilder: WhereClauseBuilder<T>? = null
    private var _sortClauseBuilder: SortClauseBuilder? = null
    private var _limit: Int = -1
    private var _offset: Int = -1

    val fieldClause get() = _fieldProjectionBuilder
    val whereClause get() = _whereClauseBuilder
    val sortClause get() = _sortClauseBuilder
    val limit get() = _limit
    val offset get() = _offset

    fun fields(init: FieldProjectionBuilder.(it: T) -> Unit) {
        _fieldProjectionBuilder = FieldProjectionBuilder()
        kClass.stubInstanceAction { _fieldProjectionBuilder?.init(it) }
    }

    fun where(init: WhereClauseBuilder<T>.(it: T) -> Unit) {
        _whereClauseBuilder = WhereClauseBuilder(kClass)
        kClass.stubInstanceAction { _whereClauseBuilder?.init(it) }
    }

    fun sort(init: SortClauseBuilder.(it: T) -> Unit) {
        _sortClauseBuilder = SortClauseBuilder()
        kClass.stubInstanceAction { _sortClauseBuilder?.init(it) }
    }

    fun limit(amount: Int) {
        _limit = amount
    }

    fun offset(amount: Int) {
        _offset = amount
    }
}