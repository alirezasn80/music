package ir.flyap.golchin_chavoshi.db

import androidx.room.Database
import androidx.room.RoomDatabase
import ir.flyap.golchin_chavoshi.db.dao.MusicDao
import ir.flyap.golchin_chavoshi.db.entity.MusicEntity

@Database(
    entities = [MusicEntity::class],
    version = 1
)
abstract class AppDB : RoomDatabase() {
    abstract val musicDao: MusicDao
}