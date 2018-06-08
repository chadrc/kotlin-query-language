package kql

import Post
import kql.statements.Insert
import kotlin.test.Test
import kotlin.test.assertEquals

class InsertTests {
    @Test
    fun testBuildInsertQuery() {
        val query = Insert<Post> {
            +Post(text = "First Post")
            +Post(text = "Second Post")
        }

        assertEquals(2, query.records.size)
    }

    @Test
    fun testInsertWithList() {
        val query = Insert<Post> {
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