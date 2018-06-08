package kql

import Post
import kotlin.test.Test
import kotlin.test.assertEquals

class KQueryInsertTests {
    @Test
    fun testBuildInsertQuery() {
        val query = KQueryInsert<Post> {
            +Post(text = "First Post")
            +Post(text = "Second Post")
        }

        assertEquals(2, query.records.size)
    }

    @Test
    fun testInsertWithList() {
        val query = KQueryInsert<Post> {
            +List(10) {
                Post(text = "Post $it")
            }
        }

        assertEquals(10, query.records.size)
    }

    @Test
    fun testInsertHelper() {
        val query = kqlInsert<Post> {
            +Post(text = "First Post")
            +Post(text = "Second Post")
        }

        assertEquals(2, query.records.size)
    }
}