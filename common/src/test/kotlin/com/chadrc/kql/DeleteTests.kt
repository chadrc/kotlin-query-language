package com.chadrc.kql

import com.chadrc.kql.models.Post
import com.chadrc.kql.statements.Delete
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

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
}