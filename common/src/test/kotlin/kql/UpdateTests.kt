package kql

import Post
import kql.statements.Update
import kotlin.test.Test
import kotlin.test.assertNotNull

class UpdateTests {

    @Test
    fun testUpdateBuilder() {
        Update(Post::class) {}
    }

    @Test
    fun testUpdateHelper() {
        val query = kqlUpdate<Post> {}
        assertNotNull(query)
    }
}