package com.chadrc.kql.mysql

import com.chadrc.kql.models.Post
import org.junit.Test
import kotlin.test.assertEquals

class MySQLInsertTests {

    @Test
    fun singleInsert() {
        val query = kqlMySQLInsert<Post, Any> {
            values {
                Post::text eq "Some Content"
                Post::topic eq "Food"
            }
        }

        assertEquals("INSERT INTO Post(text,topic) VALUES('Some Content','Food')", query.queryString)
    }

    @Test
    fun multiInsert() {
        val query = kqlMySQLInsert<Post, Any> {
            values {
                Post::text eq "Some Content"
                Post::topic eq "Food"
            }

            values {
                Post::text eq "More Content"
                Post::topic eq "Technology"
            }
        }

        assertEquals("INSERT INTO Post(text,topic) VALUES('Some Content','Food'),('More Content','Technology')", query.queryString)
    }

    @Test
    fun insertWithDifferentValues() {
        val query = kqlMySQLInsert<Post, Any> {
            values {
                Post::text eq "Some Content"
                Post::ranking eq 100
            }

            values {
                Post::text eq "More Content"
                Post::topic eq "Food"
                Post::sticky eq true
            }
        }

        assertEquals("INSERT INTO Post(ranking,sticky,text,topic) VALUES(100,NULL,'Some Content',NULL),(NULL,TRUE,'More Content','Food')", query.queryString)
    }

    class InsertInput(val text: String, val ranking: Int)

    @Test
    fun insertWithInput() {
        val query = kqlMySQLInsert<Post, InsertInput> {
            values {
                Post::text eq InsertInput::text
                Post::ranking eq InsertInput::ranking
            }
        }

        assertEquals("INSERT INTO Post(ranking,text) VALUES(?,?)", query.queryString)
        assertEquals(query.params[0], InsertInput::ranking)
        assertEquals(query.params[1], InsertInput::text)
    }
}