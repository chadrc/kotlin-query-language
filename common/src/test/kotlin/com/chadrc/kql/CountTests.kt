package com.chadrc.kql

import com.chadrc.kql.statements.Count
import kotlin.test.Test
import kotlin.test.assertEquals

class CountTests {

    @Test
    fun testCountBuilder() {
        val query = Count(Post::class) {
            where {
                it::topic eq "Food"
            }
        }

        assertEquals(1, query.conditions.size)
    }

    @Test
    fun testCountHelper() {
        val query = kqlCount<Post> {
            where {
                it::topic eq "Food"
            }
        }

        assertEquals(1, query.conditions.size)
    }
}