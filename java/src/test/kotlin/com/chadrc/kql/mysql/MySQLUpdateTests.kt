package com.chadrc.kql.mysql

import com.chadrc.kql.models.Post
import org.junit.Test
import kotlin.test.assertEquals

class MySQLUpdateTests {

    @Test
    fun updateSingleValue() {
        val query = kqlMySQLUpdate<Post, Any> {
            Post::topic toValue "Food"
        }

        assertEquals("UPDATE Post SET topic='Food'", query.queryString)
    }

    @Test
    fun updateMultipleValues() {
        val query = kqlMySQLUpdate<Post, Any> {
            Post::topic toValue "Food"
            Post::sticky toValue true
        }

        assertEquals("UPDATE Post SET topic='Food',sticky=TRUE", query.queryString)
    }

    @Test
    fun updateWithConditions() {
        val query = kqlMySQLUpdate<Post, Any> {
            Post::topic toValue "Food"
            Post::sticky toValue true

            where {
                Post::id eq 1
            }
        }

        assertEquals("UPDATE Post SET topic='Food',sticky=TRUE WHERE (id=1)", query.queryString)
    }

    @Test
    fun unsetUpdate() {
        val unary = kqlMySQLUpdate<Post, Any> {
            -Post::topic
        }

        val keyword = kqlMySQLUpdate<Post, Any> {
            unset(Post::topic)
        }

        val expected = "UPDATE Post SET topic=NULL"

        assertEquals(expected, unary.queryString)
        assertEquals(expected, keyword.queryString)
    }

    @Test
    fun addOperation() {
        val operator = kqlMySQLUpdate<Post, Any> {
            Post::ranking += 10
        }

        val keyword = kqlMySQLUpdate<Post, Any> {
            Post::ranking add 10
        }

        val expected = "UPDATE Post SET ranking=(ranking+10)"

        assertEquals(expected, operator.queryString)
        assertEquals(expected, keyword.queryString)
    }

    @Test
    fun subOperation() {
        val operator = kqlMySQLUpdate<Post, Any> {
            Post::ranking -= 10
        }

        val keyword = kqlMySQLUpdate<Post, Any> {
            Post::ranking sub 10
        }

        val expected = "UPDATE Post SET ranking=(ranking-10)"

        assertEquals(expected, operator.queryString)
        assertEquals(expected, keyword.queryString)
    }

    @Test
    fun mulOperation() {
        val operator = kqlMySQLUpdate<Post, Any> {
            Post::ranking *= 10
        }

        val keyword = kqlMySQLUpdate<Post, Any> {
            Post::ranking mul 10
        }

        val expected = "UPDATE Post SET ranking=(ranking*10)"

        assertEquals(expected, operator.queryString)
        assertEquals(expected, keyword.queryString)
    }

    @Test
    fun divOperation() {
        val operator = kqlMySQLUpdate<Post, Any> {
            Post::ranking /= 10
        }

        val keyword = kqlMySQLUpdate<Post, Any> {
            Post::ranking div 10
        }

        val expected = "UPDATE Post SET ranking=(ranking/10)"

        assertEquals(expected, operator.queryString)
        assertEquals(expected, keyword.queryString)
    }

    @Test
    fun remOperation() {
        val operator = kqlMySQLUpdate<Post, Any> {
            Post::ranking %= 10
        }

        val keyword = kqlMySQLUpdate<Post, Any> {
            Post::ranking rem 10
        }

        val expected = "UPDATE Post SET ranking=(ranking%10)"

        assertEquals(expected, operator.queryString)
        assertEquals(expected, keyword.queryString)
    }
}