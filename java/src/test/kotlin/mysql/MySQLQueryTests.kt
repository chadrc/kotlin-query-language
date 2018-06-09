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

    @Test
    fun testSelectWithWhereEq() {
        val query = kqlMySQLSelect<Post> {
            where {
                it::id eq 1
            }
        }

        assertEquals("SELECT $allPostFields FROM Post WHERE id=1", query.queryString)
    }

    @Test
    fun testSelectWithMultipleConditions() {
        val query = kqlMySQLSelect<Post> {
            where {
                it::topic eq "Food"
                it::sticky eq true
            }
        }

        assertEquals("SELECT $allPostFields FROM Post WHERE topic='Food' AND sticky=TRUE", query.queryString)
    }

    @Test
    fun testSelectWithNotEqual() {
        val query = kqlMySQLSelect<Post> {
            where {
                it::topic ne "Food"
            }
        }

        assertEquals("SELECT $allPostFields FROM Post WHERE topic!='Food'", query.queryString)
    }
}