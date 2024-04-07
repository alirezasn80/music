package ir.flyap.music_a.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ir.flyap.music_a.db.entity.MusicEntity

@Dao
interface MusicDao {

    @Delete
    suspend fun deleteMusic(music: MusicEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMusic(music: MusicEntity)

    @Query("SELECT * FROM MusicEntity")
    suspend fun getMusics(): List<MusicEntity>

    @Query("SELECT * FROM MusicEntity WHERE title LIKE :title")
    suspend fun searchMusicByTitle(title: String): List<MusicEntity>

    @Query("SELECT * FROM MusicEntity WHERE album = :album")
    suspend fun searchMusicByAlbum(album: String): List<MusicEntity>

}