package com.chadrc.kql.mysql

import com.chadrc.kql.models.Post
import kotlin.test.Test
import kotlin.test.assertEquals

class MySQLSelectTests {
    private val allPostFields = "author,authorId,id,published,ranking,sticky,text,topic"

    @Test
    fun testDefaultSelectStatement() {
        val query = kqlMySQLSelect<Post> {}

        assertEquals("SELECT $allPostFields FROM Post", query.queryString)
    }

    @Test
    fun testSelectWithPlusField() {
        val query = kqlMySQLSelect<Post> {
            fields {
                +Post::id
                +Post::authorId
            }
        }

        assertEquals("SELECT authorId,id FROM Post", query.queryString)
    }

    @Test
    fun testSelectWithMinusField() {
        val query = kqlMySQLSelect<Post> {
            fields {
                -Post::author
                -Post::ranking
            }
        }

        assertEquals("SELECT authorId,id,published,sticky,text,topic FROM Post", query.queryString)
    }

    @Test
    fun testSelectWithWhereEq() {
        val query = kqlMySQLSelect<Post> {
            where {
                Post::id eq 1
            }
        }

        assertEquals("SELECT $allPostFields FROM Post WHERE (id=1)", query.queryString)
    }

    @Test
    fun testSelectWithMultipleConditions() {
        val query = kqlMySQLSelect<Post> {
            where {
                Post::topic eq "Food"
                Post::sticky eq true
            }
        }

        assertEquals("SELECT $allPostFields FROM Post WHERE (topic='Food') AND (sticky=TRUE)", query.queryString)
    }

    @Test
    fun testSelectWithNotEqual() {
        val query = kqlMySQLSelect<Post> {
            where {
                Post::topic ne "Food"
            }
        }

        assertEquals("SELECT $allPostFields FROM Post WHERE (topic!='Food')", query.queryString)
    }

    @Test
    fun testSelectWithGreaterThan() {
        val query = kqlMySQLSelect<Post> {
            where {
                Post::ranking gt 100
            }
        }

        assertEquals("SELECT $allPostFields FROM Post WHERE (ranking>100)", query.queryString)
    }

    @Test
    fun testSelectWithGreaterThanEqual() {
        val query = kqlMySQLSelect<Post> {
            where {
                Post::ranking gte 100
            }
        }

        assertEquals("SELECT $allPostFields FROM Post WHERE (ranking>=100)", query.queryString)
    }

    @Test
    fun testSelectWithLessThan() {
        val query = kqlMySQLSelect<Post> {
            where {
                Post::ranking lt 100
            }
        }

        assertEquals("SELECT $allPostFields FROM Post WHERE (ranking<100)", query.queryString)
    }

    @Test
    fun testSelectWithLessThanEqual() {
        val query = kqlMySQLSelect<Post> {
            where {
                Post::ranking lte 100
            }
        }

        assertEquals("SELECT $allPostFields FROM Post WHERE (ranking<=100)", query.queryString)
    }

    @Test
    fun testSelectWithWithinList() {
        val query = kqlMySQLSelect<Post> {
            where {
                Post::topic within listOf("Food", "Photography", "Music")
            }
        }

        assertEquals("SELECT $allPostFields FROM Post WHERE (topic IN ('Food','Photography','Music'))", query.queryString)
    }

    @Test
    fun testSelectWithNotWithinList() {
        val query = kqlMySQLSelect<Post> {
            where {
                Post::topic notWithin listOf("Food", "Photography", "Music")
            }
        }

        assertEquals("SELECT $allPostFields FROM Post WHERE (NOT topic IN ('Food','Photography','Music'))", query.queryString)
    }

    @Test
    fun testSelectWithWithinRange() {
        val query = kqlMySQLSelect<Post> {
            where {
                Post::ranking within 100..200
            }
        }

        assertEquals("SELECT $allPostFields FROM Post WHERE (ranking BETWEEN 100 AND 200)", query.queryString)
    }

    @Test
    fun testSelectWithNotWithinRange() {
        val query = kqlMySQLSelect<Post> {
            where {
                Post::ranking notWithin 100..200
            }
        }

        assertEquals("SELECT $allPostFields FROM Post WHERE (NOT ranking BETWEEN 100 AND 200)", query.queryString)
    }

    @Test
    fun testMatches() {
        val query = kqlMySQLSelect<Post> {
            where {
                Post::text matches "Tutorial"
            }
        }

        assertEquals("SELECT $allPostFields FROM Post WHERE (text LIKE 'Tutorial')", query.queryString)
    }

    @Test
    fun testAll() {
        val query = kqlMySQLSelect<Post> {
            where {
                all {
                    Post::topic eq "Food"
                    Post::ranking gte 100
                }
            }
        }

        assertEquals("SELECT $allPostFields FROM Post WHERE ((topic='Food') AND (ranking>=100))", query.queryString)
    }

    @Test
    fun testAny() {
        val query = kqlMySQLSelect<Post> {
            where {
                any {
                    Post::topic eq "Food"
                    Post::ranking gte 100
                }
            }
        }

        assertEquals("SELECT $allPostFields FROM Post WHERE ((topic='Food') OR (ranking>=100))", query.queryString)
    }

    @Test
    fun testNestedLogicalOperators() {
        val query = kqlMySQLSelect<Post> {
            where {
                any {
                    all {
                        Post::topic eq "Food"
                        Post::ranking gte 100
                    }

                    all {
                        Post::authorId eq 10
                        Post::ranking gte 50

                        any {
                            Post::topic eq "Technology"
                            Post::topic eq "Music"
                        }
                    }
                }

                Post::text matches "Tutorial"
                Post::sticky eq true
            }
        }

        assertEquals("SELECT $allPostFields FROM Post WHERE (((topic='Food') AND (ranking>=100)) OR ((authorId=10) AND (ranking>=50) AND ((topic='Technology') OR (topic='Music')))) AND (text LIKE 'Tutorial') AND (sticky=TRUE)", query.queryString)
    }

    @Test
    fun testSortAscending() {
        val query = kqlMySQLSelect<Post> {
            sort {
                +Post::authorId
            }
        }

        assertEquals("SELECT $allPostFields FROM Post ORDER BY authorId ASC", query.queryString)
    }

    @Test
    fun testSortDescending() {
        val query = kqlMySQLSelect<Post> {
            sort {
                -Post::authorId
            }
        }

        assertEquals("SELECT $allPostFields FROM Post ORDER BY authorId DESC", query.queryString)
    }

    @Test
    fun testMultipleSorts() {
        val query = kqlMySQLSelect<Post> {
            sort {
                -Post::authorId
                +Post::ranking
            }
        }

        assertEquals("SELECT $allPostFields FROM Post ORDER BY authorId DESC,ranking ASC", query.queryString)
    }

    @Test
    fun testLimit() {
        val query = kqlMySQLSelect<Post> {
            limit(15)
        }

        assertEquals("SELECT $allPostFields FROM Post LIMIT 15", query.queryString)
    }

    @Test
    fun testOffset() {
        val query = kqlMySQLSelect<Post> {
            offset(15)
        }

        assertEquals("SELECT $allPostFields FROM Post OFFSET 15", query.queryString)
    }
}