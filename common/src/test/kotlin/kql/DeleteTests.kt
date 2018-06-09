package kql

import Post
import kql.statements.Delete
import kotlin.test.Test
import kotlin.test.assertNotNull

class DeleteTests {

    @Test
    fun testDeleteBuilder() {
        Delete(Post::class) {}
    }

    @Test
    fun testDeleteHelper() {
        val query = kqlDelete<Post> {}
        assertNotNull(query)
    }
}