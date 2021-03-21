package swu.xl.secondhouse.model

data class HouseUpDown(
    val count: Int,
    val `data`: List<HouseUpDownItem>
)

data class HouseUpDownItem(
    val City: String,
    val Date: String,
    val Lianjia: Int,
    val Lianjia_down: Int,
    val Lianjia_newsale: Int,
    val Lianjia_up: Int,
    val Lianjia_visit: Int,
    val Lianjia_visitor: Int
)