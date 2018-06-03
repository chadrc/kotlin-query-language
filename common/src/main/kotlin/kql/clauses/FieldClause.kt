package kql.clauses

import kotlin.reflect.KProperty

class KQueryFieldProjectionBuilder<T : Any> {
    private val includeFields: ArrayList<KProperty<*>> = ArrayList()
    private val excludeFields: ArrayList<KProperty<*>> = ArrayList()

    operator fun KProperty<*>.unaryPlus() {
        if (excludeFields.size > 0) {
            throw Exception("Can only include fields (+) or exclude fields (-) not both, in a single field projection.")
        }
        includeFields.add(this)
    }

    operator fun KProperty<*>.unaryMinus() {
        if (includeFields.size > 0) {
            throw Exception("Can only include fields (+) or exclude fields (-) not both, in a single field projection.")
        }
        excludeFields.add(this)
    }
}