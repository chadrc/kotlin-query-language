package com.chadrc.kql

import com.chadrc.kql.models.Post
import kotlin.test.Test

class CreateTests {

    @Test
    fun create() {
        val query = kqlInsert<Post, Any> {
            values {
                Post::text eq "Content"
            }
        }
    }
}