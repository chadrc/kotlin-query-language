package kql

import kql.statements.Insert
import kql.statements.InsertBuilder
import kql.statements.Select
import kql.statements.SelectBuilder

inline fun <reified T : Any> kqlInsert(noinline insertBuilder: InsertBuilder<T>.() -> Unit) =
        Insert(insertBuilder)

inline fun <reified T : Any> kqlSelect(noinline selectBuilder: SelectBuilder<T>.() -> Unit) =
        Select(T::class, selectBuilder)