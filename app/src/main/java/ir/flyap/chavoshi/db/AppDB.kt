package ir.flyap.chavoshi.db

import androidx.room.Database
import androidx.room.RoomDatabase
import ir.flyap.chavoshi.db.dao.MusicDao
import ir.flyap.chavoshi.db.entity.MusicEntity

@Database(
    entities = [MusicEntity::class],
    version = 1
)
abstract class AppDB : RoomDatabase() {
    abstract val musicDao: MusicDao
}