import com.chadrc.kql.models.Post
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import com.mongodb.client.model.Filters.exists
import org.bson.Document
import org.junit.Test
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.assertEquals

class MongoInsertTests {
    private lateinit var client: MongoClient
    private lateinit var testDB: MongoDatabase
    private lateinit var postCollection: MongoCollection<Document>

    // Filter guaranteed to be true for all docs
    // Passing in to prevent warning in collection methods
    private val all get() = exists("_id", true)

    @BeforeTest
    fun before() {
        client = MongoClients.create()
        testDB = client.getDatabase("test")
        postCollection = testDB.getCollection("Post")
    }

    @AfterTest
    fun after() {
        postCollection.deleteMany(all)
        client.close()
    }

    @Test
    fun insert() {
        val doc = createDocument<Post, Any> {
            values {
                Post::text eq "Some Content"
                Post::topic eq "Food"
            }
        }

        postCollection.insertOne(doc.first())

        val first: Document? = postCollection.find().first()

        assertEquals(1, postCollection.count())
        assertEquals("Some Content", first?.getString("text"))
        assertEquals("Food", first?.getString("topic"))
    }

    @Test
    fun insertMany() {
        val docs = createDocument<Post, Any> {
            for (i in 0 until 10) {
                values {
                    Post::text eq "Some Content"
                    Post::topic eq "Food"
                }
            }
        }

        postCollection.insertMany(docs)

        assertEquals(10, postCollection.count())
    }
}