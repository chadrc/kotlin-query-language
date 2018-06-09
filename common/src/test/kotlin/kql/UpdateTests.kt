package kql

import Post
import kql.statements.Unset
import kql.statements.Update
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

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
    fun testToValue() {
        val query = kqlUpdate<Post> {
            it::text toValue "Updated Text"
        }

        assertEquals(1, query.changes.size)
    }

    @Test
    fun testUnset() {
        val query = kqlUpdate<Post> {
            -it::author
        }

        assertEquals(1, query.changes.size)

        assertTrue(query.changes[0] is Unset<*>)
    }
}