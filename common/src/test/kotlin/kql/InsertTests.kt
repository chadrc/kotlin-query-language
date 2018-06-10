package kql

import Post
import kql.statements.Insert
import kotlin.test.Test
import kotlin.test.assertEquals

class InsertTests {
    @Test
    fun testBuildInsertQuery() {
        val query = Insert(Post::class) {
            values {
                it::text eq "First Post"
            }

            values {
                it::text eq "Second Post"
            }
        }

        assertEquals(2, query.records.size)
    }

//    @Test
//    fun testInsertWithList() {
//        val query = Insert<Post> {
//            +List(10) {
//                Post(text = "Post $it")
//            }
//        }
//
//        assertEquals(10, query.records.size)
//    }

    @Test
    fun testInsertHelper() {
        val query = kqlInsert<Post> {
            values {
                it::text eq "First Post"
            }

            values {
                it::text eq "Second Post"
            }
        }

        assertEquals(2, query.records.size)
    }
}