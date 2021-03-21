package swu.xl.secondhouse.sql.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "record")
data class RecordEntity (
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var u_id: Int,
    var h_id: Int,
    var city: String
)