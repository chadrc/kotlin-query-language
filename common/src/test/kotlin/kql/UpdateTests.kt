package kql

import Post
import kql.statements.MathOperation
import kql.statements.Operation
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
    fun testUnsetUnary() {
        val query = kqlUpdate<Post> {
            -it::author
        }

        assertEquals(1, query.changes.size)
        assertTrue(query.changes[0] is Unset<*>)
    }

    @Test
    fun testUnsetFunctionSyntax() {
        val query = kqlUpdate<Post> {
            unset(it::author)
        }

        assertEquals(1, query.changes.size)
        assertTrue(query.changes[0] is Unset<*>)
    }

    @Test
    fun testIncKeyword() {
        val query = kqlUpdate<Post> {
            it::ranking inc 2
        }

        assertEquals(1, query.changes.size)

        val change = query.changes[0]
        assertTrue(change is MathOperation<*>)
        assertTrue((change as MathOperation<*>).op == Operation.Increment)
    }

    @Test
    fun testIncAssignment() {
        val query = kqlUpdate<Post> {
            it::ranking += 2
        }

        assertEquals(1, query.changes.size)

        val change = query.changes[0]
        assertTrue(change is MathOperation<*>)
        assertTrue((change as MathOperation<*>).op == Operation.Increment)
    }
}