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

    @Test
    fun updateMultipleValues() {
        val query = kqlMySQLUpdate<Post> {
            it::topic toValue "Food"
            it::sticky toValue true
        }

        assertEquals("UPDATE Post SET topic='Food',sticky=TRUE", query.queryString)
    }

    @Test
    fun updateWithConditions() {
        val query = kqlMySQLUpdate<Post> {
            it::topic toValue "Food"
            it::sticky toValue true

            where {
                it::id eq 1
            }
        }

        assertEquals("UPDATE Post SET topic='Food',sticky=TRUE WHERE (id=1)", query.queryString)
    }

    @Test
    fun unsetUpdate() {
        val unary = kqlMySQLUpdate<Post> {
            -it::topic
        }

        val keyword = kqlMySQLUpdate<Post> {
            unset(it::topic)
        }

        val expected = "UPDATE Post SET topic=NULL"

        assertEquals(expected, unary.queryString)
        assertEquals(expected, keyword.queryString)
    }
}