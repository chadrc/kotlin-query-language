package kql.clauses

import kotlin.reflect.KProperty

class KQuerySortClauseBuilder<T : Any> {
    private enum class Direction {
        Ascending,
        Descending
    }

    private class Sort(val prop: KProperty<*>, val dir: Direction)

    private val sorts: ArrayList<Sort> = ArrayList()

    operator fun KProperty<*>.unaryPlus() {
        sorts.add(Sort(this, Direction.Ascending))
    }

    operator fun KProperty<*>.unaryMinus() {
        sorts.add(Sort(this, Direction.Descending))
    }
}