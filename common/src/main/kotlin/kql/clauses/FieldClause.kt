package kql.clauses

import kql.exceptions.CannotSubtractAndAddFieldsException
import kotlin.reflect.KProperty

class KQueryFieldProjectionBuilder<T : Any> {
    private val _includeFields: ArrayList<KProperty<*>> = ArrayList()
    private val _excludeFields: ArrayList<KProperty<*>> = ArrayList()

    val includedFields get() = _includeFields
    val excludedFields get() = _excludeFields

    operator fun KProperty<*>.unaryPlus() {
        if (_excludeFields.size > 0) {
            throw CannotSubtractAndAddFieldsException()
        }
        _includeFields.add(this)
    }

    operator fun KProperty<*>.unaryMinus() {
        if (_includeFields.size > 0) {
            throw CannotSubtractAndAddFieldsException()
        }
        _excludeFields.add(this)
    }
}