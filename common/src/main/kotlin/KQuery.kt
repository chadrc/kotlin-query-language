import kotlin.reflect.*

class KQueryInsert<T: Any>(val kClass: KClass<T>, val init: KQueryInsertBuilder<T>.() -> Unit)

interface KQueryInsertBuilder<T: Any> {
    fun add(record: T)
    operator fun T.unaryPlus()
}

class KQuerySelect<T: Any>(val kClass: KClass<T>, val init: KQuerySelectBuilder<T>.() -> Unit)

interface KQuerySelectBuilder<T: Any> {
    fun fields(init: KQueryFieldProjectionBuilder<T>.() -> Unit)
    fun where(init: KQueryWhereClauseBuilder<T>.() -> Unit)
    fun sort(init: KQuerySortClauseBuilder<T>.() -> Unit)
    fun limit(amount: Int)
    fun offset(amount: Int)
}

interface KQueryFieldProjectionBuilder<T: Any> {
    operator fun KProperty<*>.unaryPlus()
    operator fun KProperty<*>.unaryMinus()
}

interface KQueryWhereClauseBuilder<T: Any> {
    infix fun KProperty<Boolean>.eq(b: Boolean)
    infix fun KProperty<Byte>.eq(n: Byte)
    infix fun KProperty<Short>.eq(n: Short)
    infix fun KProperty<Int>.eq(n: Int)
    infix fun KProperty<Long>.eq(n: Long)
    infix fun KProperty<Float>.eq(n: Float)
    infix fun KProperty<Double>.eq(n: Double)
    infix fun KProperty<Char>.eq(s: Char)
    infix fun KProperty<String>.eq(s: String)
}

interface KQuerySortClauseBuilder<T: Any> {
    operator fun KProperty<*>.unaryPlus()
    operator fun KProperty<*>.unaryMinus()
}

class KQueryCount<T: Any>(val kClass: KClass<T>, val init: KQueryCountBuilder<T>.() -> Unit)

interface KQueryCountBuilder<T: Any> {
    fun where(init: KQueryWhereClauseBuilder<T>.() -> Unit)
}

class KQueryUpdate<T: Any>(val kClass: KClass<T>, val init: KQueryUpdateBuilder<T>.() -> Unit)

interface KQueryUpdateBuilder<T: Any> {
    fun set(init: KQueryMutationClauseBuilder<T>.() -> Unit)
    fun where(init: KQueryWhereClauseBuilder<T>.() -> Unit)
}

interface KQueryMutationClauseBuilder<T: Any> {
    infix fun KProperty<Boolean>.to(b: Boolean)
    infix fun KProperty<Byte>.to(n: Byte)
    infix fun KProperty<Short>.to(n: Short)
    infix fun KProperty<Int>.to(n: Int)
    infix fun KProperty<Long>.to(n: Long)
    infix fun KProperty<Float>.to(n: Float)
    infix fun KProperty<Double>.to(n: Double)
    infix fun KProperty<Char>.to(s: Char)
    infix fun KProperty<String>.to(s: String)
}

class KQueryDelete<T: Any>(val kClass: KClass<T>, val init: KQueryDeleteBuilder<T>.() -> Unit)

interface KQueryDeleteBuilder<T: Any> {
    fun where(init: KQueryWhereClauseBuilder<T>.() -> Unit)
    fun all()
}