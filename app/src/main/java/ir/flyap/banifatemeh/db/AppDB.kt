package ir.flyap.banifatemeh.db

import androidx.room.Database
import androidx.room.RoomDatabase
import ir.flyap.banifatemeh.db.dao.MusicDao
import ir.flyap.banifatemeh.db.entity.MusicEntity

@Database(
    entities = [MusicEntity::class],
    version = 1
)
abstract class AppDB : RoomDatabase() {
    abstract val musicDao: MusicDao
}