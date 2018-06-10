package com.chadrc.kql

import com.chadrc.kql.models.Post
import com.chadrc.kql.statements.Delete
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class DeleteTests {

    @Test
    fun testDeleteBuilder() {
        Delete(Post::class) {}
    }

    @Test
    fun testDeleteHelper() {
        val query = kqlDelete<Post> {}
        assertNotNull(query)
    }

    @Test
    fun testDeleteWhereClause() {
        val query = kqlDelete<Post> {
            where {
                it::id eq 1
            }
        }

        assertEquals(1, query.conditions.size)
    }

    @Test
    fun testDeleteAllFlag() {
        val query = kqlDelete<Post> {
            all()
        }

        assertEquals(true, query.deleteAll)
    }
}