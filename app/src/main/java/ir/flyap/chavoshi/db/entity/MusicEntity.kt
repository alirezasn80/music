package ir.flyap.chavoshi.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MusicEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val musicPath: String,
    val imagePath: String?,
    val lyrics: String?,
    val title: String,
    val album:String?
)