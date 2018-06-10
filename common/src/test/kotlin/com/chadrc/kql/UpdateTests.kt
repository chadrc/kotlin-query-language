package com.chadrc.kql

import com.chadrc.kql.models.Post
import com.chadrc.kql.statements.MathOperation
import com.chadrc.kql.statements.Operation
import com.chadrc.kql.statements.Unset
import com.chadrc.kql.statements.Update
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
                Post::id eq 1
            }
        }

        assertEquals(1, query.conditions.size)
    }

    @Test
    fun testToValue() {
        val query = kqlUpdate<Post> {
            Post::text toValue "Updated Text"
        }

        assertEquals(1, query.changes.size)
    }

    @Test
    fun testUnsetUnary() {
        val query = kqlUpdate<Post> {
            -Post::author
        }

        assertEquals(1, query.changes.size)
        assertTrue(query.changes[0] is Unset<*>)
    }

    @Test
    fun testUnsetFunctionSyntax() {
        val query = kqlUpdate<Post> {
            unset(Post::author)
        }

        assertEquals(1, query.changes.size)
        assertTrue(query.changes[0] is Unset<*>)
    }

    @Test
    fun testIncKeyword() {
        val query = kqlUpdate<Post> {
            Post::ranking add 2
        }

        assertEquals(1, query.changes.size)

        val change = query.changes[0]
        assertTrue(change is MathOperation<*>)
        assertTrue((change as MathOperation<*>).op == Operation.Increment)
    }

    @Test
    fun testIncAssignment() {
        val query = kqlUpdate<Post> {
            Post::ranking += 2
        }

        assertEquals(1, query.changes.size)

        val change = query.changes[0]
        assertTrue(change is MathOperation<*>)
        assertTrue((change as MathOperation<*>).op == Operation.Increment)
    }

    @Test
    fun testDecKeyword() {
        val query = kqlUpdate<Post> {
            Post::ranking sub 2
        }

        assertEquals(1, query.changes.size)

        val change = query.changes[0]
        assertTrue(change is MathOperation<*>)
        assertTrue((change as MathOperation<*>).op == Operation.Decrement)
    }

    @Test
    fun testDecAssignment() {
        val query = kqlUpdate<Post> {
            Post::ranking -= 2
        }

        assertEquals(1, query.changes.size)

        val change = query.changes[0]
        assertTrue(change is MathOperation<*>)
        assertTrue((change as MathOperation<*>).op == Operation.Decrement)
    }

    @Test
    fun testMulKeyword() {
        val query = kqlUpdate<Post> {
            Post::ranking mul 2
        }

        assertEquals(1, query.changes.size)

        val change = query.changes[0]
        assertTrue(change is MathOperation<*>)
        assertTrue((change as MathOperation<*>).op == Operation.Multiply)
    }

    @Test
    fun testMulAssignment() {
        val query = kqlUpdate<Post> {
            Post::ranking *= 2
        }

        assertEquals(1, query.changes.size)

        val change = query.changes[0]
        assertTrue(change is MathOperation<*>)
        assertTrue((change as MathOperation<*>).op == Operation.Multiply)
    }

    @Test
    fun testDivKeyword() {
        val query = kqlUpdate<Post> {
            Post::ranking div 2
        }

        assertEquals(1, query.changes.size)

        val change = query.changes[0]
        assertTrue(change is MathOperation<*>)
        assertTrue((change as MathOperation<*>).op == Operation.Divide)
    }

    @Test
    fun testDivAssignment() {
        val query = kqlUpdate<Post> {
            Post::ranking /= 2
        }

        assertEquals(1, query.changes.size)

        val change = query.changes[0]
        assertTrue(change is MathOperation<*>)
        assertTrue((change as MathOperation<*>).op == Operation.Divide)
    }

    @Test
    fun testRemKeyword() {
        val query = kqlUpdate<Post> {
            Post::ranking rem 2
        }

        assertEquals(1, query.changes.size)

        val change = query.changes[0]
        assertTrue(change is MathOperation<*>)
        assertTrue((change as MathOperation<*>).op == Operation.Remainder)
    }

    @Test
    fun testRemAssignment() {
        val query = kqlUpdate<Post> {
            Post::ranking %= 2
        }

        assertEquals(1, query.changes.size)

        val change = query.changes[0]
        assertTrue(change is MathOperation<*>)
        assertTrue((change as MathOperation<*>).op == Operation.Remainder)
    }
}