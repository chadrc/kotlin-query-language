package com.chadrc.kql.mysql

import com.chadrc.kql.clauses.Direction
import com.chadrc.kql.statements.Select
import com.chadrc.kql.statements.SelectBuilder
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

class MySQLSelect<T : Any, I : Any>(private val kClass: KClass<T>, inputClass: KClass<I>, init: SelectBuilder<T, I>.() -> Unit) {
    private val select: Select<T, I> = Select(kClass, inputClass, init)
    private val _params = ArrayList<KProperty<*>>()
    private val _queryString: String

    init {
        var fieldSelection = ""
        for (f in select.fields) {
            fieldSelection = "$fieldSelection${f.prop.name},"
        }

        // Remove trailing comma
        fieldSelection = fieldSelection.slice(0 until fieldSelection.length - 1)

        val selectClause = "SELECT $fieldSelection FROM ${kClass.simpleName}"

        val whereClause = makeWhereConditionString(select.conditions, _params)

        val sortClause = if (select.sorts.isNotEmpty()) {
            val sortList = ArrayList<String>()
            for (sort in select.sorts) {
                val dir = if (sort.dir == Direction.Ascending) "ASC" else "DESC"
                val propStr = sort.prop.name
                sortList.add("$propStr $dir")
            }
            val sortStr = sortList.joinToString(",")
            " ORDER BY $sortStr"
        } else ""

        val limitClause = if (select.limit > 0) " LIMIT ${select.limit}" else ""

        val offsetClause = if (select.offset > -1) " OFFSET ${select.offset}" else ""

        _queryString = "$selectClause$whereClause$sortClause$limitClause$offsetClause"
    }

    val queryString = _queryString
    val params get() = _params.toList()
}

inline fun <reified T : Any, reified I : Any> kqlMySQLSelect(noinline init: SelectBuilder<T, I>.() -> Unit) = MySQLSelect(T::class, I::class, init)