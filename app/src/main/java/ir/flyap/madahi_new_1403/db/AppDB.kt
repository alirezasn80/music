package ir.flyap.madahi_new_1403.db

import androidx.room.Database
import androidx.room.RoomDatabase
import ir.flyap.madahi_new_1403.db.dao.MusicDao
import ir.flyap.madahi_new_1403.db.entity.MusicEntity

@Database(
    entities = [MusicEntity::class],
    version = 1
)
abstract class AppDB : RoomDatabase() {
    abstract val musicDao: MusicDao
}