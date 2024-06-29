package ir.flyap.madahi_rasooli_golchin.db

import androidx.room.Database
import androidx.room.RoomDatabase
import ir.flyap.madahi_rasooli_golchin.db.dao.MusicDao
import ir.flyap.madahi_rasooli_golchin.db.entity.MusicEntity

@Database(
    entities = [MusicEntity::class],
    version = 1
)
abstract class AppDB : RoomDatabase() {
    abstract val musicDao: MusicDao
}