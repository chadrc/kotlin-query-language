package kql

import kotlin.reflect.*

class KQueryInsert<T: Any>(kClass: KClass<T>, init: KQueryInsertBuilder<T>.() -> Unit) {
    private val insertBuilder = KQueryInsertBuilder<T>()

    init {
        insertBuilder.init()
    }
}

class KQueryInsertBuilder<T: Any> {
    private val records = ArrayList<T>()

    fun add(record: T) {
        records.add(record)
    }

    operator fun T.unaryPlus() {
        add(this)
    }
}

class KQuerySelect<T: Any>(kClass: KClass<T>, init: KQuerySelectBuilder<T>.() -> Unit) {
    private val selectBuilder: KQuerySelectBuilder<T> = KQuerySelectBuilder(kClass)

    init {
        selectBuilder.init()
    }
}

class KQuerySelectBuilder<T: Any>(private val kClass: KClass<T>) {
    private var fieldProjectionBuilder: KQueryFieldProjectionBuilder<T>? = null
    private var whereClauseBuilder: KQueryWhereClauseBuilder<T>? = null
    private var sortClauseBuilder : KQuerySortClauseBuilder<T>? = null
    private var limit: Int = -1
    private var offset: Int = -1

    fun fields(init: KQueryFieldProjectionBuilder<T>.(it: T) -> Unit) {
        fieldProjectionBuilder = KQueryFieldProjectionBuilder()
        val primary = kClass.constructors.find { it.parameters.isEmpty() }
        if (primary != null && primary.parameters.isEmpty()) {
            val it = primary.call()
            fieldProjectionBuilder?.init(it)
        }
    }

    fun where(init: KQueryWhereClauseBuilder<T>.(it: T) -> Unit) {
        whereClauseBuilder = KQueryWhereClauseBuilder()
        val primary = kClass.constructors.find { it.parameters.isEmpty() }
        if (primary != null && primary.parameters.isEmpty()) {
            val it = primary.call()
            whereClauseBuilder?.init(it)
        }
    }

    fun sort(init: KQuerySortClauseBuilder<T>.(it: T) -> Unit) {
        sortClauseBuilder = KQuerySortClauseBuilder()
        val primary = kClass.constructors.find { it.parameters.isEmpty() }
        if (primary != null && primary.parameters.isEmpty()) {
            val it = primary.call()
            sortClauseBuilder?.init(it)
        }
    }

    fun limit(amount: Int) {
        limit = amount
    }

    fun offset(amount: Int) {
        offset = amount
    }
}

class KQueryCount<T: Any>(kClass: KClass<T>, init: KQueryCountBuilder<T>.() -> Unit) {
    private val countBuilder = KQueryCountBuilder(kClass)

    init {
        countBuilder.init()
    }
}

class KQueryCountBuilder<T: Any>(private val kClass: KClass<T>) {
    private var whereClauseBuilder: KQueryWhereClauseBuilder<T>? = null

    fun where(init: KQueryWhereClauseBuilder<T>.(it: T) -> Unit) {
        whereClauseBuilder = KQueryWhereClauseBuilder()
        val primary = kClass.constructors.find { it.parameters.isEmpty() }
        if (primary != null && primary.parameters.isEmpty()) {
            val it = primary.call()
            whereClauseBuilder?.init(it)
        }
    }
}

class KQueryUpdate<T: Any>(kClass: KClass<T>, init: KQueryUpdateBuilder<T>.() -> Unit) {
    private val updateBuilder = KQueryUpdateBuilder(kClass)

    init {
        updateBuilder.init()
    }
}

class KQueryUpdateBuilder<T: Any>(private val kClass: KClass<T>) {
    private var mutationClauseBuilder : KQueryMutationClauseBuilder<T>? = null
    private var whereClauseBuilder : KQueryWhereClauseBuilder<T>? = null

    fun set(init: KQueryMutationClauseBuilder<T>.(it: T) -> Unit) {
        mutationClauseBuilder = KQueryMutationClauseBuilder()
        val primary = kClass.constructors.find { it.parameters.isEmpty() }
        if (primary != null && primary.parameters.isEmpty()) {
            val it = primary.call()
            mutationClauseBuilder?.init(it)
        }
    }

    fun where(init: KQueryWhereClauseBuilder<T>.(it: T) -> Unit) {
        whereClauseBuilder = KQueryWhereClauseBuilder()
        val primary = kClass.constructors.find { it.parameters.isEmpty() }
        if (primary != null && primary.parameters.isEmpty()) {
            val it = primary.call()
            whereClauseBuilder?.init(it)
        }
    }
}

class KQueryDelete<T: Any>(kClass: KClass<T>, init: KQueryDeleteBuilder<T>.() -> Unit) {
    private val deleteBuilder = KQueryDeleteBuilder(kClass)

    init {
        deleteBuilder.init()
    }
}

class KQueryDeleteBuilder<T: Any>(private val kClass: KClass<T>) {
    private var whereClauseBuilder: KQueryWhereClauseBuilder<T>? = null
    private var all: Boolean = false

    fun where(init: KQueryWhereClauseBuilder<T>.(it: T) -> Unit) {
        whereClauseBuilder = KQueryWhereClauseBuilder()
        val primary = kClass.constructors.find { it.parameters.isEmpty() }
        if (primary != null && primary.parameters.isEmpty()) {
            val it = primary.call()
            whereClauseBuilder?.init(it)
        }
    }

    fun all() {
        all = true
    }
}

/**
 * Clause Builders
 */

class KQueryFieldProjectionBuilder<T: Any> {
    private val includeFields : ArrayList<KProperty<*>> = ArrayList()
    private val excludeFields : ArrayList<KProperty<*>> = ArrayList()

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

class KQueryWhereClauseBuilder<T: Any> {
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

class KQuerySortClauseBuilder<T: Any> {
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

class KQueryMutationClauseBuilder<T: Any> {
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