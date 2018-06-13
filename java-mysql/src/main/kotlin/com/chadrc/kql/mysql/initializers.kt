package com.chadrc.kql.mysql

import com.chadrc.kql.statements.*

inline fun <reified T : Any, reified I : Any> mySQLInsert(
        noinline init: InsertBuilder<T, I>.() -> Unit
): MySQLStatement {
    val insert = Insert(T::class, I::class, init)
    return MySQLStatement(insert)
}

inline fun <reified T : Any, reified I : Any> mySQLSelect(
        noinline init: SelectBuilder<T, I>.() -> Unit
): MySQLStatement {
    val select = Select(T::class, I::class, init)
    return MySQLStatement(select)
}

inline fun <reified T : Any, reified I : Any> mySQLCount(
        noinline init: CountBuilder<T, I>.() -> Unit
): MySQLStatement {
    val count = Count(T::class, I::class, init)
    return MySQLStatement(count)
}

inline fun <reified T : Any, reified I : Any> mySQLUpdate(
        noinline init: UpdateBuilder<T, I>.() -> Unit
): MySQLStatement {
    val update = Update(T::class, I::class, init)
    return MySQLStatement(update)
}

inline fun <reified T : Any, reified I : Any> mySQLDelete(
        noinline init: DeleteBuilder<T, I>.() -> Unit
): MySQLStatement {
    val delete = Delete(T::class, I::class, init)
    return MySQLStatement(delete)
}