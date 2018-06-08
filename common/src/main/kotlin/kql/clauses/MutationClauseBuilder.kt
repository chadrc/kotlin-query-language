package kql.clauses

import kotlin.reflect.KProperty

class MutationClauseBuilder<T : Any> {
    private class Assignment<T>(val prop: KProperty<T>, val value: T)

    private val assignments = ArrayList<Assignment<*>>()

    infix fun KProperty<Boolean>.to(b: Boolean) {
        assignments.add(Assignment(this, b))
    }

    infix fun KProperty<Byte>.to(n: Byte) {
        assignments.add(Assignment(this, n))
    }

    infix fun KProperty<Short>.to(n: Short) {
        assignments.add(Assignment(this, n))
    }

    infix fun KProperty<Int>.to(n: Int) {
        assignments.add(Assignment(this, n))
    }

    infix fun KProperty<Long>.to(n: Long) {
        assignments.add(Assignment(this, n))
    }

    infix fun KProperty<Float>.to(n: Float) {
        assignments.add(Assignment(this, n))
    }

    infix fun KProperty<Double>.to(n: Double) {
        assignments.add(Assignment(this, n))
    }

    infix fun KProperty<Char>.to(s: Char) {
        assignments.add(Assignment(this, s))
    }

    infix fun KProperty<String>.to(s: String) {
        assignments.add(Assignment(this, s))
    }
}