package mysql

import Post
import kotlin.test.Test
import kotlin.test.assertEquals

class MySQLQueryTests {
    private val allPostFields = "author,authorId,id,published,ranking,sticky,text,topic"

    @Test
    fun testDefaultSelectStatement() {
        val query = kqlMySQLSelect<Post> {}

        assertEquals("SELECT $allPostFields FROM Post", query.queryString)
    }

    @Test
    fun testSelectWithPlusField() {
        val query = kqlMySQLSelect<Post> {
            fields {
                +it::id
                +it::authorId
            }
        }

        assertEquals("SELECT authorId,id FROM Post", query.queryString)
    }

    @Test
    fun testSelectWithMinusField() {
        val query = kqlMySQLSelect<Post> {
            fields {
                -it::author
                -it::ranking
            }
        }

        assertEquals("SELECT authorId,id,published,sticky,text,topic FROM Post", query.queryString)
    }
}