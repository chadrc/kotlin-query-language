package kql.statements

import kql.clauses.FieldSelector
import kql.clauses.Sort
import kql.clauses.WhereClauseBuilder
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

class Select<T : Any>(private val kClass: KClass<T>, init: SelectBuilder<T>.() -> Unit) {
    private val selectBuilder: SelectBuilder<T> = SelectBuilder(kClass)

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
