package kql

import kql.statements.KQueryInsert
import kql.statements.KQueryInsertBuilder
import kql.statements.KQuerySelect
import kql.statements.KQuerySelectBuilder

inline fun <reified T : Any> kqlInsert(noinline insertBuilder: KQueryInsertBuilder<T>.() -> Unit) =
        KQueryInsert(insertBuilder)

inline fun <reified T : Any> kqlSelect(noinline selectBuilder: KQuerySelectBuilder<T>.() -> Unit) =
        KQuerySelect(T::class, selectBuilder)