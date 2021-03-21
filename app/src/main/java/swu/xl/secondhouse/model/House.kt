package swu.xl.secondhouse.model

class House : ArrayList<HouseItem>()

data class HouseItem(
    val id: String,
    val community_name: String,
    val community_url: String,
    val description: String,
    val location: String,
    val name: String,
    var picture: String,
    val price: String,
    val unit_price: String
)