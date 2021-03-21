package swu.xl.secondhouse.model

data class News(
    val `data`: List<Data>
)

data class Data(
    val City: String,
    val article_url: String,
    val create_time: String,
    val go_detail_count: Int,
    val image_url: String,
    val keyword: String,
    val source: String,
    val tag_id: Long,
    val title: String
)