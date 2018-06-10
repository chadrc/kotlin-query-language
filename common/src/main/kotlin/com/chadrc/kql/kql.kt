package com.chadrc.kql

import com.chadrc.kql.statements.*

inline fun <reified T : Any, reified I : Any> kqlInsert(noinline insertBuilder: InsertBuilder<T, I>.() -> Unit) =
        Insert(T::class, I::class, insertBuilder)

inline fun <reified T : Any, reified I : Any> kqlSelect(noinline selectBuilder: SelectBuilder<T, I>.() -> Unit) =
        Select(T::class, I::class, selectBuilder)

inline fun <reified T : Any, reified I : Any> kqlCount(noinline countBuilder: CountBuilder<T, I>.() -> Unit) =
        Count(T::class, I::class, countBuilder)

inline fun <reified T : Any, reified I : Any> kqlUpdate(noinline updateBuilder: UpdateBuilder<T, I>.() -> Unit) =
        Update(T::class, I::class, updateBuilder)

inline fun <reified T : Any, reified I : Any> kqlDelete(noinline deleteBuilder: DeleteBuilder<T, I>.() -> Unit) =
        Delete(T::class, I::class, deleteBuilder)