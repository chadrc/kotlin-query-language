package com.chadrc.kql.mysql

import com.chadrc.kql.models.Post
import org.junit.Test
import kotlin.test.assertEquals

class MySQLDeleteTests {
    @Test
    fun deleteCondition() {
        val query = kqlMySQLDelete<Post> {
            where {
                Post::id eq 1
            }
        }

        assertEquals("DELETE FROM Post WHERE (id=1)", query.queryString)
    }

    @Test
    fun deleteAll() {
        val query = kqlMySQLDelete<Post> {
            all()
        }

        assertEquals("DELETE FROM Post", query.queryString)
    }

    @Test
    fun blankWithNoWhereOrAll() {
        val query = kqlMySQLDelete<Post> { }

        assertEquals("", query.queryString)
    }
}