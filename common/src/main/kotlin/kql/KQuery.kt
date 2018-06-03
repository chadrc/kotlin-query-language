package kql

inline fun <reified T : Any> kqlInsert(noinline insertBuilder: KQueryInsertBuilder<T>.() -> Unit) =
        KQueryInsert(T::class, insertBuilder)
