package com.chadrc.kql.mysql

import com.chadrc.kql.models.Post
import org.junit.Test
import kotlin.test.assertEquals

class MySQLDeleteTests {
    @Test
    fun deleteCondition() {
        val query = mySQLDelete<Post, Any> {
            where {
                Post::id eq 1
            }
        }

        assertEquals("DELETE FROM Post WHERE (id=1)", query.queryString)
    }

    @Test
    fun deleteAll() {
        val query = mySQLDelete<Post, Any> {
            all()
        }

        assertEquals("DELETE FROM Post", query.queryString)
    }

    @Test
    fun blankWithNoWhereOrAll() {
        val query = mySQLDelete<Post, Any> { }

        assertEquals("", query.queryString)
    }

    class DeleteInput(val id: Int)

    @Test
    fun withInput() {
        val query = mySQLDelete<Post, DeleteInput> {
            where {
                Post::id eq DeleteInput::id
            }
        }

        assertEquals("DELETE FROM Post WHERE (id=?)", query.queryString)
        assertEquals(query.params[0], DeleteInput::id)
    }
}