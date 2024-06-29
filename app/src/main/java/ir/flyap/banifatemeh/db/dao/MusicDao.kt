package ir.flyap.banifatemeh.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ir.flyap.banifatemeh.db.entity.MusicEntity

@Dao
interface MusicDao {

    @Delete
    suspend fun deleteMusic(music: MusicEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMusic(music: MusicEntity)

    @Query("SELECT * FROM MusicEntity")
    suspend fun getAllMusic(): List<MusicEntity>

    @Query("SELECT album FROM MusicEntity group by album")
    suspend fun getCategoriesByAlbum(): List<String>

    @Query("SELECT * FROM MusicEntity WHERE title LIKE :title")
    suspend fun searchMusic(title: String): List<MusicEntity>

    @Query("SELECT * FROM MusicEntity WHERE album = :album")
    suspend fun getMusics(album: String): List<MusicEntity>

}