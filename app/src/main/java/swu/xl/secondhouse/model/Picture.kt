package swu.xl.secondhouse.model

class Picture : ArrayList<PictureItem>()

data class PictureItem(
    val description: String,
    val picture: String
)