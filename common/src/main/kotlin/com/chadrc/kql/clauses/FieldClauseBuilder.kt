package com.chadrc.kql.clauses

import com.chadrc.kql.exceptions.CannotSubtractAndAddFieldsException
import kotlin.reflect.KProperty

interface FieldSelector {
    val includedFields: ArrayList<FieldProjection>?
    val excludedFields: ArrayList<FieldProjection>?
}

class FieldProjection(
        val prop: KProperty<*>,
        override val includedFields: ArrayList<FieldProjection>? = null,
        override val excludedFields: ArrayList<FieldProjection>? = null
) : FieldSelector

class FieldClauseBuilder : FieldSelector {

    private val _includeFields: ArrayList<FieldProjection> = ArrayList()
    private val _excludeFields: ArrayList<FieldProjection> = ArrayList()

    override val includedFields get() = _includeFields
    override val excludedFields get() = _excludeFields

    operator fun KProperty<*>.unaryPlus() {
        if (_excludeFields.size > 0) {
            throw CannotSubtractAndAddFieldsException()
        }
        _includeFields.add(FieldProjection(this))
    }

    operator fun KProperty<*>.unaryMinus() {
        if (_includeFields.size > 0) {
            throw CannotSubtractAndAddFieldsException()
        }
        _excludeFields.add(FieldProjection(this))
    }

    infix fun <P : Any> KProperty<P?>.withFields(
            init: FieldClauseBuilder.() -> Unit
    ) {
        val builder = FieldClauseBuilder()
        builder.init()
        includedFields.add(FieldProjection(this, builder.includedFields, builder.excludedFields))
    }
}