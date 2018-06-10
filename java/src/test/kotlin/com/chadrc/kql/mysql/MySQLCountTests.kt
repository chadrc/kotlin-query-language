package com.chadrc.kql.mysql

import com.chadrc.kql.Post
import org.junit.Test
import kotlin.test.assertEquals

class MySQLCountTests {

    @Test
    fun countAll() {
        val query = kqlMySQLCount<Post> {}

        assertEquals("SELECT COUNT(*) FROM Post", query.queryString)
    }

    @Test
    fun countWithConditions() {
        val query = kqlMySQLCount<Post> {
            where {
                it::ranking gt 100
                it::topic eq "Food"
            }
        }

        assertEquals("SELECT COUNT(*) FROM Post WHERE (ranking>100) AND (topic='Food')", query.queryString)
    }
}