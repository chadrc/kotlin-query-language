package com.chadrc.kql

import com.chadrc.kql.statements.*

inline fun <reified T : Any, reified I : Any> kqlInsert(noinline insertBuilder: InsertBuilder<T, I>.() -> Unit) =
        Insert(T::class, I::class, insertBuilder)

inline fun <reified T : Any> kqlSelect(noinline selectBuilder: SelectBuilder<T>.() -> Unit) =
        Select(T::class, selectBuilder)

inline fun <reified T : Any> kqlCount(noinline countBuilder: CountBuilder<T>.() -> Unit) =
        Count(T::class, countBuilder)

inline fun <reified T : Any> kqlUpdate(noinline updateBuilder: UpdateBuilder<T>.() -> Unit) =
        Update(T::class, updateBuilder)

inline fun <reified T : Any> kqlDelete(noinline deleteBuilder: DeleteBuilder<T>.() -> Unit) =
        Delete(T::class, deleteBuilder)