package com.chadrc.kql

import com.chadrc.kql.exceptions.LeftPropOperandNotOnQueryClass
import com.chadrc.kql.exceptions.RightPropOperandNotOnInputClass
import com.chadrc.kql.models.Author
import com.chadrc.kql.models.Post
import com.chadrc.kql.statements.Count
import kotlin.reflect.KProperty
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class CountTests {

    @Test
    fun testCountBuilder() {
        val query = Count(Post::class, Any::class) {
            where {
                Post::topic eq "Food"
            }
        }

        assertEquals(1, query.conditions.size)
    }

    @Test
    fun testCountHelper() {
        val query = kqlCount<Post, Any> {
            where {
                Post::topic eq "Food"
            }
        }

        assertEquals(1, query.conditions.size)
    }

    class CountInput(val search: String, val minRank: Int)

    @Test
    fun selectWithInput() {
        val query = kqlCount<Post, CountInput> {
            where {
                Post::text matches CountInput::search
                Post::ranking gte CountInput::minRank
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
            kqlCount<Post, Any> {
                where {
                    Author::firstName eq "John"
                }
            }
        }
    }

    @Test
    fun errorWhenUsingNonInputProperty() {
        assertFailsWith<RightPropOperandNotOnInputClass> {
            kqlCount<Post, CountInput> {
                where {
                    Post::text eq Author::firstName
                }
            }
        }
    }
}