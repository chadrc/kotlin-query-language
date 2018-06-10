package com.chadrc.kql

import com.chadrc.kql.exceptions.LeftPropOperandNotOnQueryClass
import com.chadrc.kql.exceptions.RightPropOperandNotOnInputClass
import com.chadrc.kql.models.Author
import com.chadrc.kql.models.Post
import com.chadrc.kql.statements.Delete
import kotlin.reflect.KProperty
import kotlin.test.*

class DeleteTests {

    @Test
    fun testDeleteBuilder() {
        Delete(Post::class, Any::class) {}
    }

    @Test
    fun testDeleteHelper() {
        val query = kqlDelete<Post, Any> {}
        assertNotNull(query)
    }

    @Test
    fun testDeleteWhereClause() {
        val query = kqlDelete<Post, Any> {
            where {
                Post::id eq 1
            }
        }

        assertEquals(1, query.conditions.size)
    }

    @Test
    fun testDeleteAllFlag() {
        val query = kqlDelete<Post, Any> {
            all()
        }

        assertEquals(true, query.deleteAll)
    }

    class DeleteInput(val search: String, val minRank: Int)

    @Test
    fun selectWithInput() {
        val query = kqlDelete<Post, DeleteInput> {
            where {
                Post::text matches DeleteInput::search
                Post::ranking gte DeleteInput::minRank
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
            kqlDelete<Post, Any> {
                where {
                    Author::firstName eq "John"
                }
            }
        }
    }

    @Test
    fun errorWhenUsingNonInputProperty() {
        assertFailsWith<RightPropOperandNotOnInputClass> {
            kqlDelete<Post, DeleteInput> {
                where {
                    Post::text eq Author::firstName
                }
            }
        }
    }
}