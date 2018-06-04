package kql

inline fun <reified T : Any> kqlInsert(noinline insertBuilder: KQueryInsertBuilder<T>.() -> Unit) =
        KQueryInsert(T::class, insertBuilder)

inline fun <reified T : Any> kqlSelect(noinline selectBuilder: KQuerySelectBuilder<T>.() -> Unit) =
        KQuerySelect(T::class, selectBuilder)