package kql

import Post
import kql.statements.Update
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class UpdateTests {

    @Test
    fun testUpdateBuilder() {
        Update(Post::class) {}
    }

    @Test
    fun testUpdateHelper() {
        val query = kqlUpdate<Post> {}
        assertNotNull(query)
    }

    @Test
    fun testUpdateWhereClause() {
        val query = kqlUpdate<Post> {
            where {
                it::id eq 1
            }
        }

        assertEquals(1, query.conditions!!.size)
    }

    @Test
    fun testUpdateChanges() {
        val query = kqlUpdate<Post> {
            it::text toValue "Updated Text"
        }

        assertEquals(1, query.changes.size)
    }
}