package swu.xl.secondhouse.sql.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import swu.xl.secondhouse.sql.dao.LoveDao
import swu.xl.secondhouse.sql.dao.RecordDao
import swu.xl.secondhouse.sql.dao.UserDao
import swu.xl.secondhouse.sql.entity.LoveEntity
import swu.xl.secondhouse.sql.entity.RecordEntity
import swu.xl.secondhouse.sql.entity.UserEntity

@Database(entities = [UserEntity::class, RecordEntity::class, LoveEntity::class], version = 6)
abstract class SecondHouseRoomBase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun recordDao(): RecordDao
    abstract fun loveDao(): LoveDao

    companion object {
        @Volatile
        private var INSTANCE: SecondHouseRoomBase? =null

        fun getRoomBase(context: Context): SecondHouseRoomBase {
            val temp = INSTANCE

            if (temp != null) return temp

            synchronized(this) {
                return INSTANCE ?: Room.databaseBuilder(
                    context,
                    SecondHouseRoomBase::class.java,
                    "second.house.db"
                ).fallbackToDestructiveMigration().build().also {
                    INSTANCE = it
                }
            }
        }
    }
}