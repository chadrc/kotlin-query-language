package mysql

import Post
import kotlin.test.Test
import kotlin.test.assertEquals

class MySQLQueryTests {
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
                +it::id
                +it::authorId
            }
        }

        assertEquals("SELECT authorId,id FROM Post", query.queryString)
    }

    @Test
    fun testSelectWithMinusField() {
        val query = kqlMySQLSelect<Post> {
            fields {
                -it::author
                -it::ranking
            }
        }

        assertEquals("SELECT authorId,id,published,sticky,text,topic FROM Post", query.queryString)
    }

    @Test
    fun testSelectWithWhereEq() {
        val query = kqlMySQLSelect<Post> {
            where {
                it::id eq 1
            }
        }

        assertEquals("SELECT $allPostFields FROM Post WHERE (id=1)", query.queryString)
    }

    @Test
    fun testSelectWithMultipleConditions() {
        val query = kqlMySQLSelect<Post> {
            where {
                it::topic eq "Food"
                it::sticky eq true
            }
        }

        assertEquals("SELECT $allPostFields FROM Post WHERE (topic='Food') AND (sticky=TRUE)", query.queryString)
    }

    @Test
    fun testSelectWithNotEqual() {
        val query = kqlMySQLSelect<Post> {
            where {
                it::topic ne "Food"
            }
        }

        assertEquals("SELECT $allPostFields FROM Post WHERE (topic!='Food')", query.queryString)
    }

    @Test
    fun testSelectWithGreaterThan() {
        val query = kqlMySQLSelect<Post> {
            where {
                it::ranking gt 100
            }
        }

        assertEquals("SELECT $allPostFields FROM Post WHERE (ranking>100)", query.queryString)
    }

    @Test
    fun testSelectWithGreaterThanEqual() {
        val query = kqlMySQLSelect<Post> {
            where {
                it::ranking gte 100
            }
        }

        assertEquals("SELECT $allPostFields FROM Post WHERE (ranking>=100)", query.queryString)
    }

    @Test
    fun testSelectWithLessThan() {
        val query = kqlMySQLSelect<Post> {
            where {
                it::ranking lt 100
            }
        }

        assertEquals("SELECT $allPostFields FROM Post WHERE (ranking<100)", query.queryString)
    }

    @Test
    fun testSelectWithLessThanEqual() {
        val query = kqlMySQLSelect<Post> {
            where {
                it::ranking lte 100
            }
        }

        assertEquals("SELECT $allPostFields FROM Post WHERE (ranking<=100)", query.queryString)
    }

    @Test
    fun testSelectWithWithinList() {
        val query = kqlMySQLSelect<Post> {
            where {
                it::topic within listOf("Food", "Photography", "Music")
            }
        }

        assertEquals("SELECT $allPostFields FROM Post WHERE (topic IN ('Food','Photography','Music'))", query.queryString)
    }

    @Test
    fun testSelectWithNotWithinList() {
        val query = kqlMySQLSelect<Post> {
            where {
                it::topic notWithin listOf("Food", "Photography", "Music")
            }
        }

        assertEquals("SELECT $allPostFields FROM Post WHERE (NOT topic IN ('Food','Photography','Music'))", query.queryString)
    }

    @Test
    fun testSelectWithWithinRange() {
        val query = kqlMySQLSelect<Post> {
            where {
                it::ranking within 100..200
            }
        }

        assertEquals("SELECT $allPostFields FROM Post WHERE (ranking BETWEEN 100 AND 200)", query.queryString)
    }

    @Test
    fun testSelectWithNotWithinRange() {
        val query = kqlMySQLSelect<Post> {
            where {
                it::ranking notWithin 100..200
            }
        }

        assertEquals("SELECT $allPostFields FROM Post WHERE (NOT ranking BETWEEN 100 AND 200)", query.queryString)
    }

    @Test
    fun testMatches() {
        val query = kqlMySQLSelect<Post> {
            where {
                it::text matches "Tutorial"
            }
        }

        assertEquals("SELECT $allPostFields FROM Post WHERE (text LIKE 'Tutorial')", query.queryString)
    }

    @Test
    fun testAll() {
        val query = kqlMySQLSelect<Post> {
            where {
                all {
                    it::topic eq "Food"
                    it::ranking gte 100
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
                    it::topic eq "Food"
                    it::ranking gte 100
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
                        it::topic eq "Food"
                        it::ranking gte 100
                    }

                    all {
                        it::authorId eq 10
                        it::ranking gte 50

                        any {
                            it::topic eq "Technology"
                            it::topic eq "Music"
                        }
                    }
                }

                it::text matches "Tutorial"
                it::sticky eq true
            }
        }

        assertEquals("SELECT $allPostFields FROM Post WHERE (((topic='Food') AND (ranking>=100)) OR ((authorId=10) AND (ranking>=50) AND ((topic='Technology') OR (topic='Music')))) AND (text LIKE 'Tutorial') AND (sticky=TRUE)", query.queryString)
    }

    @Test
    fun testSortAscending() {
        val query = kqlMySQLSelect<Post> {
            sort {
                +it::authorId
            }
        }

        assertEquals("SELECT $allPostFields FROM Post ORDER BY authorId ASC", query.queryString)
    }

    @Test
    fun testSortDescending() {
        val query = kqlMySQLSelect<Post> {
            sort {
                -it::authorId
            }
        }

        assertEquals("SELECT $allPostFields FROM Post ORDER BY authorId DESC", query.queryString)
    }

    @Test
    fun testMultipleSorts() {
        val query = kqlMySQLSelect<Post> {
            sort {
                -it::authorId
                +it::ranking
            }
        }

        assertEquals("SELECT $allPostFields FROM Post ORDER BY authorId DESC,ranking ASC", query.queryString)
    }
}