package com.chadrc.kql.mysql

import com.chadrc.kql.models.Post
import org.junit.Test
import kotlin.test.assertEquals

class MySQLDeleteTests {
    @Test
    fun deleteCondition() {
        val query = kqlMySQLDelete<Post, Any> {
            where {
                Post::id eq 1
            }
        }

        assertEquals("DELETE FROM Post WHERE (id=1)", query.queryString)
    }

    @Test
    fun deleteAll() {
        val query = kqlMySQLDelete<Post, Any> {
            all()
        }

        assertEquals("DELETE FROM Post", query.queryString)
    }

    @Test
    fun blankWithNoWhereOrAll() {
        val query = kqlMySQLDelete<Post, Any> { }

        assertEquals("", query.queryString)
    }

    class DeleteInput(val id: Int)

    @Test
    fun withInput() {
        val query = kqlMySQLDelete<Post, DeleteInput> {
            where {
                Post::id eq DeleteInput::id
            }
        }

        assertEquals("DELETE FROM Post WHERE (id=?)", query.queryString)
        assertEquals(query.params[0], DeleteInput::id)
    }
}