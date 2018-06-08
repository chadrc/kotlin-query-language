package kql.clauses

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
        _sorts.add(Sort(this, Direction.Ascending))
    }

    operator fun KProperty<*>.unaryMinus() {
        _sorts.add(Sort(this, Direction.Descending))
    }
}