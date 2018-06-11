package com.chadrc.kql.mysql

import com.chadrc.kql.models.Post
import org.junit.Test
import java.sql.Connection
import java.sql.DriverManager
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.assertEquals

class ExecutorTests {
    private var conn: Connection? = null

    @BeforeTest
    fun before() {
        Class.forName("com.mysql.cj.jdbc.Driver").newInstance()
        conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/test?user=root&password=password"
        )
    }

    @AfterTest
    fun after() {
        val deleteAllStatement = conn?.createStatement()
        deleteAllStatement?.execute("DELETE FROM Post")
        conn?.close()
    }

    @Test
    fun insert() {
        val query = kqlMySQLInsert<Post, Any> {
            values {
                Post::authorId eq 0
                Post::text eq "Content"
                Post::sticky eq false
                Post::topic eq "Food"
                Post::published eq 0
                Post::ranking eq 100
            }
        }

        val kqlStatement = conn?.createStatement()
        kqlStatement?.execute(query.queryString)

        val statement = conn?.createStatement()
        val resultSet = statement?.executeQuery("SELECT * FROM Post")
        resultSet?.next()
        assertEquals("Content", resultSet?.getString("text"))
    }
}