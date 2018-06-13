package com.chadrc.kql.mysql

import com.chadrc.kql.models.Post
import com.chadrc.kql.mysql.executor.MySQLKQLExecutor
import com.chadrc.kql.statements.InsertBuilder
import org.junit.Test
import java.sql.ResultSet
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.assertEquals

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
            insertSet()
        }

        val resultSet = executor?.select {
            fields {
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
            val text = resultSet?.getString("text")
            val ranking = resultSet?.getInt("ranking")

            assertEquals("Post $i", text)
            assertEquals(100 + (i * 10), ranking)
            resultSet?.next()
        }
    }

    class SelectInput(val minRanking: Int)

    @Test
    fun selectPrepared() {
        executor?.insert {
            insertSet()
        }

        val prepared = executor?.prepareSelect(SelectInput::class) {
            fields {
                +Post::text
                +Post::ranking
            }

            where {
                Post::ranking gt SelectInput::minRanking
            }

            sort {
                -Post::ranking
            }
        }

        val resultSet = prepared?.executeQuery(SelectInput(140))

        resultSet?.next()
        for (i in 9 downTo 5) {
            val text = resultSet?.getString("text")
            val ranking = resultSet?.getInt("ranking")

            assertEquals("Post $i", text)
            assertEquals(100 + (i * 10), ranking)
            resultSet?.next()
        }
    }

    @Test
    fun count() {
        executor?.insert { insertSet() }

        val resultSet = executor?.count {
            where {
                Post::ranking gt 140
            }
        }

        resultSet?.next()
        val count = resultSet?.getInt("COUNT(*)")

        assertEquals(5, count)
    }

    @Test
    fun preparedCount() {
        executor?.insert { insertSet() }

        val prepared = executor?.prepareCount(SelectInput::class) {
            where {
                Post::ranking gt SelectInput::minRanking
            }
        }

        val resultSet = prepared?.executeQuery(SelectInput(140))

        resultSet?.next()
        val count = resultSet?.getInt("COUNT(*)")

        assertEquals(5, count)
    }

    @Test
    fun update() {
        executor?.insert { insertSet() }

        val result = executor?.update {
            Post::text toValue "Updated Value"

            where {
                Post::ranking gt 140
            }
        }

        assertEquals(5, result)

        val resultSet = executor?.select {
            fields {
                -Post::author
            }

            where {
                Post::text eq "Updated Value"
            }
        }

        var count = 0
        while (resultSet?.next()!!) {
            count++
        }

        assertEquals(5, count)
    }

    class UpdateTextInput(val text: String)

    @Test
    fun preparedUpdate() {
        executor?.insert { insertSet() }

        val prepared = executor?.prepareUpdate(UpdateTextInput::class) {
            Post::text toValue UpdateTextInput::text

            where {
                Post::ranking gt 140
            }
        }

        val result = prepared?.executeUpdate(UpdateTextInput("Updated Value"))

        assertEquals(5, result)

        val resultSet = executor?.select {
            fields {
                -Post::author
            }

            where {
                Post::text eq "Updated Value"
            }
        }

        var count = 0
        while (resultSet?.next()!!) {
            count++
        }

        assertEquals(5, count)
    }

    @Test
    fun delete() {
        executor?.insert { insertSet() }

        val result = executor?.delete {
            where {
                Post::ranking gt 140
            }
        }

        assertEquals(5, result)
    }

    class DeleteInput(val minRanking: Int)

    @Test
    fun preparedDelete() {
        executor?.insert { insertSet() }

        val prepared = executor?.prepareDelete(DeleteInput::class) {
            where {
                Post::ranking gt DeleteInput::minRanking
            }
        }

        val result = prepared?.executeUpdate(DeleteInput(140))

        assertEquals(5, result)
    }

    private fun InsertBuilder<Post, *>.insertSet() {
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
}