package ir.flyap.pooyanfar.db

import androidx.room.Database
import androidx.room.RoomDatabase
import ir.flyap.pooyanfar.db.dao.MusicDao
import ir.flyap.pooyanfar.db.entity.MusicEntity

@Database(
    entities = [MusicEntity::class],
    version = 1
)
abstract class AppDB : RoomDatabase() {
    abstract val musicDao: MusicDao
}