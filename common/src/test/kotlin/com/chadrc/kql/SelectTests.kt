package com.chadrc.kql

import com.chadrc.kql.clauses.WhereClauseBuilder
import com.chadrc.kql.exceptions.CannotSortSamePropertyTwice
import com.chadrc.kql.exceptions.CannotSubtractAndAddFieldsException
import com.chadrc.kql.exceptions.NoStubConstructorException
import com.chadrc.kql.models.Author
import com.chadrc.kql.models.NoStubModel
import com.chadrc.kql.models.Post
import com.chadrc.kql.statements.Select
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class SelectTests {
    @Test
    fun testSelectBuilder() {
        val query = Select(Post::class) {
            fields {
                +Post::id
                +Post::text
            }
        }

        assertEquals(2, query.fields.size)
    }

    @Test
    fun testSelectBuilderHelper() {
        val query = kqlSelect<Post> {
            fields {
                +Post::id
            }
        }

        assertEquals(1, query.fields.size)
    }

    @Test
    fun testMinusField() {
        val query = kqlSelect<Post> {
            fields {
                -Post::id
            }
        }

        assertEquals(7, query.fields.size)
    }

    @Test
    fun testCannotSpecifyPlusAndMinusFields() {
        assertFailsWith<CannotSubtractAndAddFieldsException> {
            Select(Post::class) {
                fields {
                    +Post::text
                    -Post::id
                }
            }
        }
    }

    @Test
    fun testSelectBuilderWithInvalidModel() {
        assertFailsWith<NoStubConstructorException> {
            Select(NoStubModel::class) {
                fields {
                    +Post::id
                }
            }
        }
    }

    @Test
    fun testSubObjectWithFields() {
        val query = kqlSelect<Post> {
            fields {
                Post::author withFields {
                    +Author::firstName
                    +Author::lastName
                }
            }
        }

        assertEquals(1, query.fields.size)
        val authorSet = query.fields[0]
        assertEquals(2, authorSet.subFields?.size)
    }

    @Test
    fun testWhereClause() {
        val query = kqlSelect<Post> {
            where {
                Post::id eq 0
            }
        }

        assertEquals(1, query.conditions.size)
    }

    @Test
    fun testAllWhereComparisonOperators() {
        val minDate = 1514764800000
        val maxDate = 1517270400000

        val query = kqlSelect<Post> {
            where {
                Post::id eq 0
                Post::id ne 0

                Post::published gt minDate
                Post::published gte minDate

                Post::published lt maxDate
                Post::published lte maxDate

                Post::published within minDate..maxDate
                Post::published notWithin minDate..maxDate

                Post::topic within listOf("Food", "Photography", "Music")
                Post::topic notWithin listOf("Food", "Photography", "Music")

                Post::text matches "Tutorial"
            }
        }

        assertEquals(11, query.conditions.size)
    }

    @Test
    fun testWhereLogicOperators() {
        val minDate = 1514764800000
        val maxDate = 1517270400000

        val query = kqlSelect<Post> {
            where {
                all {
                    Post::topic eq "Food"
                    Post::text matches "Tutorial"
                }

                any {
                    Post::topic eq "Food"
                    Post::published within minDate..maxDate
                }
            }
        }

        assertEquals(2, query.conditions.size)

        @Suppress("UNCHECKED_CAST")
        val allList = query.conditions[0].value as List<WhereClauseBuilder.Condition>
        assertEquals(2, (allList.size))

        @Suppress("UNCHECKED_CAST")
        val anyList = query.conditions[1].value as List<WhereClauseBuilder.Condition>
        assertEquals(2, (anyList.size))
    }

    @Test
    fun testSortClause() {
        val query = kqlSelect<Post> {
            sort {
                +Post::published
                -Post::author
            }
        }

        assertEquals(2, query.sorts.size)
    }

    @Test
    fun testCannotSortSamePropTwice() {
        assertFailsWith<CannotSortSamePropertyTwice> {
            kqlSelect<Post> {
                sort {
                    +Post::published
                    -Post::published
                }
            }
        }
    }

    @Test
    fun testLimit() {
        val query = kqlSelect<Post> {
            limit(10)
        }

        assertEquals(query.limit, 10)
    }

    @Test
    fun testOffset() {
        val query = kqlSelect<Post> {
            offset(10)
        }

        assertEquals(query.offset, 10)
    }
}