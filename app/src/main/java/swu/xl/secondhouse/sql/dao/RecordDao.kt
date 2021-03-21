package swu.xl.secondhouse.sql.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import swu.xl.secondhouse.sql.entity.RecordEntity

@Dao
interface RecordDao {
    @Delete
    fun deleteRecord(record: RecordEntity)

    @Query("DELETE from record")
    fun deleteAll()

    @Insert
    fun insertRecord(record: RecordEntity)

    @Query("SELECT * FROM record limit :offset, 10")
    fun queryRecord(offset: Int): List<RecordEntity>

    @Query("SELECT * FROM record WHERE u_id = :user limit :offset, 10")
    fun queryRecord(offset: Int, user: Int): List<RecordEntity>

    @Query("SELECT * FROM record WHERE u_id = :user")
    fun queryAllRecord(user: Int): List<RecordEntity>
}