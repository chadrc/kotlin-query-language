class Post(
        val id: Int = -1,
        val author: Author = Author(),
        val text: String = "",
        val sticky: Boolean = false,
        val authorId: Int = -1,
        val topic: String = "",
        val published: Long = 0
)