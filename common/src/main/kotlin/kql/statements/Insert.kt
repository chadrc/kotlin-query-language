package kql.statements

class Insert<T : Any>(init: InsertBuilder<T>.() -> Unit) {
    private val insertBuilder = InsertBuilder<T>()

    val records = insertBuilder.records

    init {
        insertBuilder.init()
    }
}
