package swu.xl.secondhouse.model

data class Index(
    val `data`: List<IndexItem>
)

data class IndexItem(
    val City: String,
    val Ehuanbi: Double,
    val Etongbi: Double,
    val Time: String,
    val Xhuanbi: Double,
    val Xtongbi: Double
)