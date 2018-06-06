package kql.clauses

import kql.exceptions.CannotSubtractAndAddFieldsException
import kql.utils.stubInstanceAction
import kotlin.reflect.KProperty

class KQueryFieldProjectionBuilder<T : Any> {
    class FieldProjection(
            val prop: KProperty<*>,
            val includeSubFields: ArrayList<FieldProjection>? = null,
            val excludeSubFields: ArrayList<FieldProjection>? = null
    )

    private val _includeFields: ArrayList<FieldProjection> = ArrayList()
    private val _excludeFields: ArrayList<FieldProjection> = ArrayList()

    val includedFields get() = _includeFields
    val excludedFields get() = _excludeFields

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

    infix fun <P : Any> KProperty<P>.withFields(init: KQueryFieldProjectionBuilder<P>.(it: P) -> Unit) {
        val builder = KQueryFieldProjectionBuilder<P>()
        stubInstanceAction<P> { builder.init(it) }
        _includeFields.add(FieldProjection(this, builder.includedFields, builder.excludedFields))
    }
}