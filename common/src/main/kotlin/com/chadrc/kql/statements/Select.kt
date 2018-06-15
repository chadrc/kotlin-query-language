package com.chadrc.kql.statements

import com.chadrc.kql.clauses.FieldSelector
import com.chadrc.kql.clauses.Sort
import com.chadrc.kql.clauses.WhereClauseBuilder
import com.chadrc.kql.utils.getMembers
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

class Select<T : Any, I : Any>(private val _kClass: KClass<T>, private val inputClass: KClass<I>, init: SelectBuilder<T, I>.() -> Unit) {
    private val selectBuilder: SelectBuilder<T, I> = SelectBuilder(_kClass, inputClass)

    class Field(val prop: KProperty<*>, val subFields: List<Field>? = null)

    val fields: List<Field>
        get() {
            val allProperties = getMembers(_kClass).filterIsInstance<KProperty<*>>()
            val clause = selectBuilder.fieldClause ?: return allProperties.map { Field(it) }

            return getProperties(_kClass, clause)
        }

    val conditions: List<WhereClauseBuilder.Condition> get() = selectBuilder.whereClause?.conditions ?: listOf()

    val sorts: List<Sort> get() = selectBuilder.sortClause?.sorts ?: listOf()

    val limit get() = selectBuilder.limit

    val offset get() = selectBuilder.offset

    val kClass get() = _kClass

    init {
        selectBuilder.init()
    }

    private fun <T : Any> getProperties(kClass: KClass<T>, selector: FieldSelector): List<Field> {
        val allProperties = getMembers(kClass).filterIsInstance<KProperty<*>>()

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
