package swu.xl.secondhouse.sql.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class UserEntity (
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var nickname: String,
    var picture: String,
    var account: String,
    var password: String
)