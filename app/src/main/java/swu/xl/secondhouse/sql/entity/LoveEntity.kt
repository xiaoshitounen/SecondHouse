package swu.xl.secondhouse.sql.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "love")
data class LoveEntity (
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var u_id: Int,
    var h_id: Int,
    var city: String
)