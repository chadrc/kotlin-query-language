package kql.clauses

import kotlin.reflect.KProperty

class KQueryWhereClauseBuilder<T : Any> {
    private enum class Operator {
        Equals
    }

    private class Condition(val prop: KProperty<*>, val op: Operator, val value: Any)

    private val conditions: ArrayList<Condition> = ArrayList()

    infix fun KProperty<Boolean>.eq(b: Boolean) {
        conditions.add(Condition(this, Operator.Equals, b))
    }

    infix fun KProperty<Byte>.eq(n: Byte) {
        conditions.add(Condition(this, Operator.Equals, n))
    }

    infix fun KProperty<Short>.eq(n: Short) {
        conditions.add(Condition(this, Operator.Equals, n))
    }

    infix fun KProperty<Int>.eq(n: Int) {
        conditions.add(Condition(this, Operator.Equals, n))
    }

    infix fun KProperty<Long>.eq(n: Long) {
        conditions.add(Condition(this, Operator.Equals, n))
    }

    infix fun KProperty<Float>.eq(n: Float) {
        conditions.add(Condition(this, Operator.Equals, n))
    }

    infix fun KProperty<Double>.eq(n: Double) {
        conditions.add(Condition(this, Operator.Equals, n))
    }

    infix fun KProperty<Char>.eq(s: Char) {
        conditions.add(Condition(this, Operator.Equals, s))
    }

    infix fun KProperty<String>.eq(s: String) {
        conditions.add(Condition(this, Operator.Equals, s))
    }
}