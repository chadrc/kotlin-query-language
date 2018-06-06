class Post(
        val id: Int = -1,
        val text: String = "",
        val sticky: Boolean = false,
        val authorId: Int = -1,
        val topic: String = "",
        val published: Long = 0
)