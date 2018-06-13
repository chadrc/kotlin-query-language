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

    class UpdateWhereInput(val id: Int)

    @Test
    fun whereInput() {
        val query = kqlMySQLUpdate<Post, UpdateWhereInput> {
            Post::topic toValue "Food"
            where {
                Post::id eq UpdateWhereInput::id
            }
        }

        assertEquals("UPDATE Post SET topic='Food' WHERE (id=?)", query.queryString)
        assertEquals(query.params[0], UpdateWhereInput::id)
    }

    class UpdateChangeInput(val topic: String, val ranking: Int)

    @Test
    fun changeInput() {
        val query = kqlMySQLUpdate<Post, UpdateChangeInput> {
            Post::topic toValue UpdateChangeInput::topic
            Post::ranking toValue UpdateChangeInput::ranking
        }

        assertEquals("UPDATE Post SET topic=?,ranking=?", query.queryString)
        assertEquals(query.params[0], UpdateChangeInput::topic)
        assertEquals(query.params[1], UpdateChangeInput::ranking)
    }
}