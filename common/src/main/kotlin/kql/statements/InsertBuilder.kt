package kql.statements

class InsertBuilder<T : Any> {
    private val _records = ArrayList<T>()

    val records get() = _records

    operator fun T.unaryPlus() {
        _records.add(this)
    }

    operator fun Collection<T>.unaryPlus() {
        _records.addAll(this)
    }
}