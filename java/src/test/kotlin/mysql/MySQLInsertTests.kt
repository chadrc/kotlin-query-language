package mysql

import Post
import org.junit.Test
import kotlin.test.assertEquals

class MySQLInsertTests {

    @Test
    fun singleInsert() {
        val query = kqlMySQLInsert<Post> {
            values {
                it::text eq "Some Content"
                it::topic eq "Food"
            }
        }

        assertEquals("INSERT INTO Post(text,topic) VALUES('Some Content','Food')", query.queryString)
    }

    @Test
    fun multiInsert() {
        val query = kqlMySQLInsert<Post> {
            values {
                it::text eq "Some Content"
                it::topic eq "Food"
            }

            values {
                it::text eq "More Content"
                it::topic eq "Technology"
            }
        }

        assertEquals("INSERT INTO Post(text,topic) VALUES('Some Content','Food'),('More Content','Technology')", query.queryString)
    }

    @Test
    fun insertWithDifferentValues() {
        val query = kqlMySQLInsert<Post> {
            values {
                it::text eq "Some Content"
                it::ranking eq 100
            }

            values {
                it::text eq "More Content"
                it::topic eq "Food"
                it::sticky eq true
            }
        }

        assertEquals("INSERT INTO Post(ranking,sticky,text,topic) VALUES(100,NULL,'Some Content',NULL),(NULL,TRUE,'More Content','Food')", query.queryString)
    }
}