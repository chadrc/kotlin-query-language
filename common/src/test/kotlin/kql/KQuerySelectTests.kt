package kql

import Author
import Post
import kql.exceptions.CannotSubtractAndAddFieldsException
import kql.exceptions.NoStubConstructorException
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class KQuerySelectTests {
    @Test
    fun testSelectBuilder() {
        val query = KQuerySelect(Post::class) {
            fields {
                +it::id
                +it::text
            }
        }

        assertEquals(2, query.fields.size)
    }

    @Test
    fun testSelectBuilderHelper() {
        val query = kqlSelect<Post> {
            fields {
                +it::id
            }
        }

        assertEquals(1, query.fields.size)
    }

    @Test
    fun testMinusField() {
        val query = kqlSelect<Post> {
            fields {
                -it::id
            }
        }

        assertEquals(3, query.fields.size)
    }

    @Test
    fun testCannotSpecifyPlusAndMinusFields() {
        assertFailsWith<CannotSubtractAndAddFieldsException> {
            KQuerySelect(Post::class) {
                fields {
                    +it::text
                    -it::id
                }
            }
        }
    }

    @Test
    fun testSelectBuilderWithInvalidModel() {
        assertFailsWith<NoStubConstructorException> {
            KQuerySelect(Author::class) {
                fields {
                    +it::firstName
                }
            }
        }
    }
}