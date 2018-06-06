package kql

import NoStubModel
import Post
import kql.clauses.KQueryWhereClauseBuilder
import kql.exceptions.CannotSubtractAndAddFieldsException
import kql.exceptions.NoStubConstructorException
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class KQuerySelectTests {
    @Test
    fun testSelectBuilder() {
        val query = KQuerySelect(Post::class) {
            fields {
                +it::id
                +it::text
            }
        }

        assertEquals(2, query.fields.size)
    }

    @Test
    fun testSelectBuilderHelper() {
        val query = kqlSelect<Post> {
            fields {
                +it::id
            }
        }

        assertEquals(1, query.fields.size)
    }

    @Test
    fun testMinusField() {
        val query = kqlSelect<Post> {
            fields {
                -it::id
            }
        }

        assertEquals(5, query.fields.size)
    }

    @Test
    fun testCannotSpecifyPlusAndMinusFields() {
        assertFailsWith<CannotSubtractAndAddFieldsException> {
            KQuerySelect(Post::class) {
                fields {
                    +it::text
                    -it::id
                }
            }
        }
    }

    @Test
    fun testSelectBuilderWithInvalidModel() {
        assertFailsWith<NoStubConstructorException> {
            KQuerySelect(NoStubModel::class) {
                fields {
                    +it::id
                }
            }
        }
    }

    @Test
    fun testWhereClause() {
        val query = kqlSelect<Post> {
            where {
                it::id eq 0
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
                it::id eq 0
                it::id ne 0

                it::published gt minDate
                it::published gte minDate

                it::published lt maxDate
                it::published lte maxDate

                it::published within minDate..maxDate
                it::published notWithin minDate..maxDate

                it::topic within listOf("Food", "Photography", "Music")
                it::topic notWithin listOf("Food", "Photography", "Music")

                it::text matches "Tutorial"
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
                    it::topic eq "Food"
                    it::text matches "Tutorial"
                }

                any {
                    it::topic eq "Food"
                    it::published within minDate..maxDate
                }
            }
        }

        assertEquals(2, query.conditions.size)

        val allList = query.conditions[0].value as List<KQueryWhereClauseBuilder.Condition>
        assertEquals(2, (allList.size))

        val anyList = query.conditions[1].value as List<KQueryWhereClauseBuilder.Condition>
        assertEquals(2, (anyList.size))
    }
}