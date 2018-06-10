package com.chadrc.kql.mysql

import com.chadrc.kql.Post
import org.junit.Test
import kotlin.test.assertEquals

class MySQLUpdateTests {

    @Test
    fun updateSingleValue() {
        val query = kqlMySQLUpdate<Post> {
            it::topic toValue "Food"
        }

        assertEquals("UPDATE Post SET topic='Food'", query.queryString)
    }
}