package com.chadrc.kql.mysql

import com.chadrc.kql.models.Post
import com.chadrc.kql.mysql.executor.MySQLKQLExecutor
import org.junit.Test
import java.sql.ResultSet
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class ExecutorTests {
    private var executor: MySQLKQLExecutor<Post>? = null

    init {
        Class.forName("com.mysql.cj.jdbc.Driver").newInstance()
    }

    @BeforeTest
    fun before() {
        executor = MySQLKQLExecutor(Post::class)
    }

    @AfterTest
    fun after() {
        val deleteAllStatement = executor?.conn?.createStatement()
        deleteAllStatement?.execute("DELETE FROM Post")
        executor?.conn?.close()
    }

    @Test
    fun insert() {
        executor?.insert {
            values {
                Post::authorId eq 0
                Post::text eq "Content"
                Post::sticky eq false
                Post::topic eq "Food"
                Post::published eq 0
                Post::ranking eq 100
            }
        }

        val resultSet = selectAllPosts()
        assertEquals("Content", resultSet?.getString("text"))
    }

    class InsertInput(val text: String, val topic: String)

    @Test
    fun insertPrepared() {
        val prepared = executor?.prepareInsert(InsertInput::class) {
            values {
                Post::authorId eq 0
                Post::sticky eq false
                Post::published eq 0
                Post::ranking eq 100
                Post::text eq InsertInput::text
                Post::topic eq InsertInput::topic
            }
        }

        val input = InsertInput("Prepared Content", "Technology")
        prepared?.execute(input)

        val resultSet = selectAllPosts()
        assertEquals("Prepared Content", resultSet?.getString("text"))
        assertEquals("Technology", resultSet?.getString("topic"))
    }

    private fun selectAllPosts(): ResultSet? {
        // Use raw jdbc api to test inserts
        // select kql tested next
        val statement = executor?.conn?.createStatement()
        val resultSet = statement?.executeQuery("SELECT * FROM Post")
        resultSet?.next()
        return resultSet
    }

    @Test
    fun select() {
        executor?.insert {
            for (i in 0 until 10) {
                values {
                    Post::authorId eq 0
                    Post::text eq "Post $i"
                    Post::sticky eq false
                    Post::topic eq "Food"
                    Post::published eq 0
                    Post::ranking eq 100 + (i * 10)
                }
            }
        }

        val resultSet = executor?.select {
            fields {
                +Post::id
                +Post::text
                +Post::ranking
            }

            where {
                Post::ranking gt 140
            }

            sort {
                -Post::ranking
            }
        }

        resultSet?.next()
        for (i in 9 downTo 5) {
            val id = resultSet?.getInt("id")
            val text = resultSet?.getString("text")
            val ranking = resultSet?.getInt("ranking")

            assertNotEquals(0, id)
            assertEquals("Post $i", text)
            assertEquals(100 + (i * 10), ranking)
            resultSet?.next()
        }
    }
}