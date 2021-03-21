package swu.xl.secondhouse.model

class Detail : ArrayList<DetailItem>()

data class DetailItem(
    val key: String,
    val value: String
)