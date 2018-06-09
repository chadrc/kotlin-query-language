package kql

import kql.statements.*

inline fun <reified T : Any> kqlInsert(noinline insertBuilder: InsertBuilder<T>.() -> Unit) =
        Insert(insertBuilder)

inline fun <reified T : Any> kqlSelect(noinline selectBuilder: SelectBuilder<T>.() -> Unit) =
        Select(T::class, selectBuilder)

inline fun <reified T : Any> kqlCount(noinline countBuilder: CountBuilder<T>.() -> Unit) =
        Count(T::class, countBuilder)

inline fun <reified T : Any> kqlUpdate(noinline updateBuilder: UpdateBuilder<T>.(it: T) -> Unit) =
        Update(T::class, updateBuilder)

inline fun <reified T : Any> kqlDelete(noinline deleteBuilder: DeleteBuilder<T>.() -> Unit) =
        Delete(T::class, deleteBuilder)