package kql.clauses

import kotlin.reflect.KProperty

class KQueryWhereClauseBuilder<T : Any> {
    enum class Operator {
        Equals
    }

    class Condition(val prop: KProperty<*>, val op: Operator, val value: Any)

    private val _conditions: ArrayList<Condition> = ArrayList()

    val conditions get() = _conditions

    infix fun KProperty<Boolean>.eq(b: Boolean) {
        _conditions.add(Condition(this, Operator.Equals, b))
    }

    infix fun KProperty<Byte>.eq(n: Byte) {
        _conditions.add(Condition(this, Operator.Equals, n))
    }

    infix fun KProperty<Short>.eq(n: Short) {
        _conditions.add(Condition(this, Operator.Equals, n))
    }

    infix fun KProperty<Int>.eq(n: Int) {
        _conditions.add(Condition(this, Operator.Equals, n))
    }

    infix fun KProperty<Long>.eq(n: Long) {
        _conditions.add(Condition(this, Operator.Equals, n))
    }

    infix fun KProperty<Float>.eq(n: Float) {
        _conditions.add(Condition(this, Operator.Equals, n))
    }

    infix fun KProperty<Double>.eq(n: Double) {
        _conditions.add(Condition(this, Operator.Equals, n))
    }

    infix fun KProperty<Char>.eq(s: Char) {
        _conditions.add(Condition(this, Operator.Equals, s))
    }

    infix fun KProperty<String>.eq(s: String) {
        _conditions.add(Condition(this, Operator.Equals, s))
    }
}