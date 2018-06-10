package com.chadrc.kql

import com.chadrc.kql.exceptions.LeftPropOperandNotOnQueryClass
import com.chadrc.kql.exceptions.RightPropOperandNotOnInputClass
import com.chadrc.kql.models.Author
import com.chadrc.kql.models.Post
import com.chadrc.kql.statements.MathOperation
import com.chadrc.kql.statements.Operation
import com.chadrc.kql.statements.Unset
import com.chadrc.kql.statements.Update
import kotlin.reflect.KProperty
import kotlin.test.*

class UpdateTests {

    @Test
    fun testUpdateBuilder() {
        Update(Post::class, Any::class) {}
    }

    @Test
    fun testUpdateHelper() {
        val query = kqlUpdate<Post, Any> {}
        assertNotNull(query)
    }

    @Test
    fun testUpdateWhereClause() {
        val query = kqlUpdate<Post, Any> {
            where {
                Post::id eq 1
            }
        }

        assertEquals(1, query.conditions.size)
    }

    @Test
    fun testToValue() {
        val query = kqlUpdate<Post, Any> {
            Post::text toValue "Updated Text"
        }

        assertEquals(1, query.changes.size)
    }

    @Test
    fun testUnsetUnary() {
        val query = kqlUpdate<Post, Any> {
            -Post::author
        }

        assertEquals(1, query.changes.size)
        assertTrue(query.changes[0] is Unset<*>)
    }

    @Test
    fun testUnsetFunctionSyntax() {
        val query = kqlUpdate<Post, Any> {
            unset(Post::author)
        }

        assertEquals(1, query.changes.size)
        assertTrue(query.changes[0] is Unset<*>)
    }

    @Test
    fun testIncKeyword() {
        val query = kqlUpdate<Post, Any> {
            Post::ranking add 2
        }

        assertEquals(1, query.changes.size)

        val change = query.changes[0]
        assertTrue(change is MathOperation<*>)
        assertTrue((change as MathOperation<*>).op == Operation.Increment)
    }

    @Test
    fun testIncAssignment() {
        val query = kqlUpdate<Post, Any> {
            Post::ranking += 2
        }

        assertEquals(1, query.changes.size)

        val change = query.changes[0]
        assertTrue(change is MathOperation<*>)
        assertTrue((change as MathOperation<*>).op == Operation.Increment)
    }

    @Test
    fun testDecKeyword() {
        val query = kqlUpdate<Post, Any> {
            Post::ranking sub 2
        }

        assertEquals(1, query.changes.size)

        val change = query.changes[0]
        assertTrue(change is MathOperation<*>)
        assertTrue((change as MathOperation<*>).op == Operation.Decrement)
    }

    @Test
    fun testDecAssignment() {
        val query = kqlUpdate<Post, Any> {
            Post::ranking -= 2
        }

        assertEquals(1, query.changes.size)

        val change = query.changes[0]
        assertTrue(change is MathOperation<*>)
        assertTrue((change as MathOperation<*>).op == Operation.Decrement)
    }

    @Test
    fun testMulKeyword() {
        val query = kqlUpdate<Post, Any> {
            Post::ranking mul 2
        }

        assertEquals(1, query.changes.size)

        val change = query.changes[0]
        assertTrue(change is MathOperation<*>)
        assertTrue((change as MathOperation<*>).op == Operation.Multiply)
    }

    @Test
    fun testMulAssignment() {
        val query = kqlUpdate<Post, Any> {
            Post::ranking *= 2
        }

        assertEquals(1, query.changes.size)

        val change = query.changes[0]
        assertTrue(change is MathOperation<*>)
        assertTrue((change as MathOperation<*>).op == Operation.Multiply)
    }

    @Test
    fun testDivKeyword() {
        val query = kqlUpdate<Post, Any> {
            Post::ranking div 2
        }

        assertEquals(1, query.changes.size)

        val change = query.changes[0]
        assertTrue(change is MathOperation<*>)
        assertTrue((change as MathOperation<*>).op == Operation.Divide)
    }

    @Test
    fun testDivAssignment() {
        val query = kqlUpdate<Post, Any> {
            Post::ranking /= 2
        }

        assertEquals(1, query.changes.size)

        val change = query.changes[0]
        assertTrue(change is MathOperation<*>)
        assertTrue((change as MathOperation<*>).op == Operation.Divide)
    }

    @Test
    fun testRemKeyword() {
        val query = kqlUpdate<Post, Any> {
            Post::ranking rem 2
        }

        assertEquals(1, query.changes.size)

        val change = query.changes[0]
        assertTrue(change is MathOperation<*>)
        assertTrue((change as MathOperation<*>).op == Operation.Remainder)
    }

    @Test
    fun testRemAssignment() {
        val query = kqlUpdate<Post, Any> {
            Post::ranking %= 2
        }

        assertEquals(1, query.changes.size)

        val change = query.changes[0]
        assertTrue(change is MathOperation<*>)
        assertTrue((change as MathOperation<*>).op == Operation.Remainder)
    }

    class UpdateInput(val search: String, val minRank: Int)

    @Test
    fun withWhereClauseInput() {
        val query = kqlUpdate<Post, UpdateInput> {
            where {
                Post::text matches UpdateInput::search
                Post::ranking gte UpdateInput::minRank
            }
        }

        val conditions = query.conditions
        val one = conditions[0]
        val two = conditions[1]
        assertTrue(one.value is KProperty<*>)
        assertTrue(two.value is KProperty<*>)
    }

    @Test
    fun errorWhenUsingNonModelProperty() {
        assertFailsWith<LeftPropOperandNotOnQueryClass> {
            kqlUpdate<Post, Any> {
                where {
                    Author::firstName eq "John"
                }
            }
        }
    }

    @Test
    fun errorWhenUsingNonInputProperty() {
        assertFailsWith<RightPropOperandNotOnInputClass> {
            kqlUpdate<Post, UpdateInput> {
                where {
                    Post::text eq Author::firstName
                }
            }
        }
    }
}