package kql.clauses

import kotlin.reflect.KProperty

class KQueryWhereClauseBuilder<T : Any> {
    enum class Operator {
        Equals,
        NotEquals,
        LessThan,
        LessThanOrEquals,
        GreaterThan,
        GreaterThanOrEquals,
        Within,
        NotWithin,
        Matches
    }

    class Condition(val prop: KProperty<*>, val op: Operator, val value: Any)

    private val _conditions: ArrayList<Condition> = ArrayList()

    val conditions get() = _conditions

    infix fun <T : Comparable<T>> KProperty<T>.eq(v: T) {
        _conditions.add(Condition(this, Operator.Equals, v))
    }

    infix fun <T : Comparable<T>> KProperty<T>.ne(v: T) {
        _conditions.add(Condition(this, Operator.NotEquals, v))
    }

    infix fun <T : Comparable<T>> KProperty<T>.gt(v: T) {
        _conditions.add(Condition(this, Operator.LessThan, v))
    }

    infix fun <T : Comparable<T>> KProperty<T>.gte(v: T) {
        _conditions.add(Condition(this, Operator.GreaterThanOrEquals, v))
    }

    infix fun <T : Comparable<T>> KProperty<T>.lt(v: T) {
        _conditions.add(Condition(this, Operator.LessThan, v))
    }

    infix fun <T : Comparable<T>> KProperty<T>.lte(v: T) {
        _conditions.add(Condition(this, Operator.LessThanOrEquals, v))
    }

    infix fun <T : Comparable<T>> KProperty<T>.within(v: ClosedRange<T>) {
        _conditions.add(Condition(this, Operator.Within, v))
    }

    infix fun <T : Comparable<T>> KProperty<T>.within(v: Collection<T>) {
        _conditions.add(Condition(this, Operator.Within, v))
    }

    infix fun <T : Comparable<T>> KProperty<T>.notWithin(v: ClosedRange<T>) {
        _conditions.add(Condition(this, Operator.NotWithin, v))
    }

    infix fun <T : Comparable<T>> KProperty<T>.notWithin(v: Collection<T>) {
        _conditions.add(Condition(this, Operator.NotWithin, v))
    }

    infix fun KProperty<String>.matches(s: String) {
        _conditions.add(Condition(this, Operator.Matches, s))
    }
}