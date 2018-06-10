package com.chadrc.kql.clauses

import com.chadrc.kql.exceptions.CannotSortSamePropertyTwice
import kotlin.reflect.KProperty

enum class Direction {
    Ascending,
    Descending
}

class Sort(val prop: KProperty<*>, val dir: Direction)

class SortClauseBuilder {
    private val _sorts: ArrayList<Sort> = ArrayList()

    val sorts get() = _sorts

    operator fun KProperty<*>.unaryPlus() {
        assertPropertyNotSorted(this)
        _sorts.add(Sort(this, Direction.Ascending))
    }

    operator fun KProperty<*>.unaryMinus() {
        assertPropertyNotSorted(this)
        _sorts.add(Sort(this, Direction.Descending))
    }

    private fun assertPropertyNotSorted(prop: KProperty<*>) {
        val existing = _sorts.find { it.prop == prop }
        if (existing != null) {
            throw CannotSortSamePropertyTwice()
        }
    }
}