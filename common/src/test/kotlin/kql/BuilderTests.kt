package kql

import Post
import kotlin.test.Test
import kotlin.test.assertEquals

class BuilderTests {
    @Test
    fun testBuildInsertQuery() {
        val query = KQueryInsert(Post::class) {
            +Post("First Post")
            +Post("Second Post")
        }

        assertEquals(2, query.records.size)
    }
}