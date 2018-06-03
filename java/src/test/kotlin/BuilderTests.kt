import kql.KQueryInsert
import kql.KQuerySelect
import kotlin.test.Test

class BuilderTests {

    @Test
    fun buildInsertQuery() {
        val query = KQueryInsert(Post::class) {

        }
    }

    @Test
    fun buildSelectQuery() {
        val query = KQuerySelect(Post::class) {
            fields {
                +it::text
            }

            where {
                it::text eq "Some Text"
            }

            sort {
                +it::text
            }
        }
    }
}