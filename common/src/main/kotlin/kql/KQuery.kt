package kql

import kql.statements.Insert
import kql.statements.KQueryInsertBuilder
import kql.statements.KQuerySelectBuilder
import kql.statements.Select

inline fun <reified T : Any> kqlInsert(noinline insertBuilder: KQueryInsertBuilder<T>.() -> Unit) =
        Insert(insertBuilder)

inline fun <reified T : Any> kqlSelect(noinline selectBuilder: KQuerySelectBuilder<T>.() -> Unit) =
        Select(T::class, selectBuilder)