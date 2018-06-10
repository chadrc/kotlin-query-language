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
}