package com.chadrc.kql

import com.chadrc.kql.exceptions.LeftPropOperandNotOnQueryClass
import com.chadrc.kql.statements.Insert
import kotlin.reflect.KProperty
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class InsertTests {
    @Test
    fun testBuildInsertQuery() {
        val query = Insert(Post::class, Nothing::class) {
            values {
                Post::text eq "First Post"
            }

            values {
                Post::text eq "Second Post"
            }
        }

        assertEquals(2, query.records.size)
    }

    @Test
    fun testInsertHelper() {
        val query = kqlInsert<Post, Any> {
            values {
                Post::text eq "First Post"
            }

            values {
                Post::text eq "Second Post"
            }
        }

        assertEquals(2, query.records.size)
    }

    @Test
    fun errorWhenUsingPropNotOnClass() {
        assertFailsWith<LeftPropOperandNotOnQueryClass> {
            kqlInsert<Post, Any> {
                values {
                    Author::firstName eq "John"
                }
            }
        }
    }

    class PostTextInput(val text: String)

    @Test
    fun testInput() {
        val query = kqlInsert<Post, PostTextInput> {
            values {
                Post::text eq PostTextInput::text
            }
        }

        val firstRecord = query.records[0]
        val firstValuePair = firstRecord.valuePairs[0]
        assertTrue(firstValuePair.value is KProperty<*>)
    }

    @Test
    fun errorWhenUsingPropNotOnClassWithInput() {
        assertFailsWith<LeftPropOperandNotOnQueryClass> {
            kqlInsert<Post, Any> {
                values {
                    Author::firstName eq PostTextInput::text
                }
            }
        }
    }
}