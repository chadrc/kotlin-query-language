package kql.statements

import kql.clauses.MutationClauseBuilder
import kql.clauses.WhereClauseBuilder
import kql.utils.stubInstanceAction
import kotlin.reflect.KClass

class UpdateBuilder<T : Any>(private val kClass: KClass<T>) {
    private var mutationClauseBuilder: MutationClauseBuilder<T>? = null
    private var whereClauseBuilder: WhereClauseBuilder<T>? = null

    val conditions get() = whereClauseBuilder?.conditions

    fun set(init: MutationClauseBuilder<T>.(it: T) -> Unit) {
        mutationClauseBuilder = MutationClauseBuilder()
        kClass.stubInstanceAction { mutationClauseBuilder?.init(it) }
    }

    fun where(init: WhereClauseBuilder<T>.(it: T) -> Unit) {
        whereClauseBuilder = WhereClauseBuilder(kClass)
        kClass.stubInstanceAction { whereClauseBuilder?.init(it) }
    }
}