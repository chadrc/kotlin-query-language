package kql.clauses

import kotlin.reflect.KProperty

class KQueryFieldProjectionBuilder<T : Any> {
    private val _includeFields: ArrayList<KProperty<*>> = ArrayList()
    private val _excludeFields: ArrayList<KProperty<*>> = ArrayList()

    val includedFields get() = _includeFields
    val excludedFields get() = _excludeFields

    operator fun KProperty<*>.unaryPlus() {
        println(this)
        if (_excludeFields.size > 0) {
            throw Exception("Can only include fields (+) or exclude fields (-) not both, in a single field projection.")
        }
        println(this)
        _includeFields.add(this)
    }

    operator fun KProperty<*>.unaryMinus() {
        if (_includeFields.size > 0) {
            throw Exception("Can only include fields (+) or exclude fields (-) not both, in a single field projection.")
        }
        _excludeFields.add(this)
    }
}