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
}