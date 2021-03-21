package swu.xl.secondhouse.sql.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import swu.xl.secondhouse.sql.entity.UserEntity

@Dao
interface UserDao {
    @Insert
    fun insertUser(user: UserEntity)

    @Update
    fun updateUser(user: UserEntity)

    @Query("SELECT * FROM user")
    fun queryUser(): List<UserEntity>

    @Query("SELECT * FROM user WHERE id = :user")
    fun queryUser(user: Int): List<UserEntity>
}