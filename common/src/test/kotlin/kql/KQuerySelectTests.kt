package kql

import Author
import Post
import kql.exceptions.NoStubConstructorException
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.fail

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
    fun testSelectBuilderWithInvalidModel() {
        try {
            KQuerySelect(Author::class) {
                fields {
                    +it::firstName
                }
            }
        } catch (exception: NoStubConstructorException) {
            return
        }

        fail("Excepted ${NoStubConstructorException::class.simpleName} to be thrown.")
    }
}