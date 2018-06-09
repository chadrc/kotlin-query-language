package mysql

import Post
import kotlin.test.Test
import kotlin.test.assertEquals

class MySQLQueryTests {

    @Test
    fun testDefaultSelectStatement() {
        val query = kqlMySQLSelect<Post> {}

        assertEquals("SELECT * FROM Post", query.queryString)
    }
}