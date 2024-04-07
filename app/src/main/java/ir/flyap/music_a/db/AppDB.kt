package ir.flyap.music_a.db

import androidx.room.Database
import androidx.room.RoomDatabase
import ir.flyap.music_a.db.dao.MusicDao
import ir.flyap.music_a.db.entity.MusicEntity

@Database(
    entities = [MusicEntity::class],
    version = 1
)
abstract class AppDB : RoomDatabase() {
    abstract val musicDao: MusicDao
}