package swu.xl.secondhouse.sql.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import swu.xl.secondhouse.sql.entity.LoveEntity
import swu.xl.secondhouse.sql.entity.RecordEntity

@Dao
interface LoveDao {
    @Delete
    fun deleteLove(love: LoveEntity)

    @Query("DELETE FROM love")
    fun deleteAll()

    @Insert
    fun insertLove(love: LoveEntity)

    @Query("SELECT * FROM love limit :offset, 10")
    fun queryLove(offset: Int): List<LoveEntity>

    @Query("SELECT * FROM love WHERE u_id = :user limit :offset, 10")
    fun queryLove(offset: Int, user: Int): List<LoveEntity>

    @Query("SELECT * FROM love WHERE u_id = :user AND h_id = :house")
    fun querySimpleLove(user: Int, house: Int): List<LoveEntity>

    @Query("SELECT * FROM love WHERE u_id = :user")
    fun queryAllLove(user: Int): List<LoveEntity>
}