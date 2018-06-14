import com.chadrc.kql.statements.Insert
import com.chadrc.kql.statements.InsertBuilder
import org.bson.Document

inline fun <reified T : Any, reified I : Any> createDocument(
        noinline init: InsertBuilder<T, I>.() -> Unit
): List<Document> {
    val insert = Insert(T::class, I::class, init)
    val docs = ArrayList<Document>()

    for (record in insert.records) {
        val doc = Document()

        for (valuePair in record.valuePairs) {
            doc.append(valuePair.prop.name, valuePair.value)
        }

        docs.add(doc)
    }

    return docs.toList()
}