package com.chadrc.kql

import com.chadrc.kql.statements.Input
import com.chadrc.kql.statements.Insert
import com.chadrc.kql.statements.input
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class InsertTests {
    @Test
    fun testBuildInsertQuery() {
        val query = Insert(Post::class) {
            values {
                it::text eq "First Post"
            }

            values {
                it::text eq "Second Post"
            }
        }

        assertEquals(2, query.records.size)
    }

    @Test
    fun testInsertHelper() {
        val query = kqlInsert<Post> {
            values {
                it::text eq "First Post"
            }

            values {
                it::text eq "Second Post"
            }
        }

        assertEquals(2, query.records.size)
    }

    @Test
    fun testInput() {
        val query = kqlInsert<Post> {
            values {
                it::text eq input("text")
            }
        }

        val firstRecord = query.records[0]
        val firstValuePair = firstRecord.valuePairs[0]
        assertTrue(firstValuePair.value is Input<*>)
    }
}