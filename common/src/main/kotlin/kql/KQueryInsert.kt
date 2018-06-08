package kql

class KQueryInsert<T : Any>(init: KQueryInsertBuilder<T>.() -> Unit) {
    private val insertBuilder = KQueryInsertBuilder<T>()

    val records = insertBuilder.records

    init {
        insertBuilder.init()
    }
}

class KQueryInsertBuilder<T : Any> {
    private val _records = ArrayList<T>()

    val records get() = _records

    operator fun T.unaryPlus() {
        _records.add(this)
    }

    operator fun Collection<T>.unaryPlus() {
        _records.addAll(this)
    }
}