package com.chadrc.kql.mysql

import com.chadrc.kql.models.Post
import org.junit.Test
import kotlin.test.assertEquals

class MySQLCountTests {

    @Test
    fun countAll() {
        val query = kqlMySQLCount<Post, Any> {}

        assertEquals("SELECT COUNT(*) FROM Post", query.queryString)
    }

    @Test
    fun countWithConditions() {
        val query = kqlMySQLCount<Post, Any> {
            where {
                Post::ranking gt 100
                Post::topic eq "Food"
            }
        }

        assertEquals("SELECT COUNT(*) FROM Post WHERE (ranking>100) AND (topic='Food')", query.queryString)
    }

    class CountInput(val minRanking: Int, val topic: String)

    @Test
    fun withInput() {
        val query = kqlMySQLCount<Post, CountInput> {
            where {
                Post::ranking gte CountInput::minRanking
                Post::topic eq CountInput::topic
            }
        }

        assertEquals("SELECT COUNT(*) FROM Post WHERE (ranking>=?) AND (topic=?)", query.queryString)
        assertEquals(query.params[0], CountInput::minRanking)
        assertEquals(query.params[1], CountInput::topic)
    }
}