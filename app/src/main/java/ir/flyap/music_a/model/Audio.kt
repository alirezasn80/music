package ir.flyap.music_a.model

import android.net.Uri

data class Audio(
    val id: String,
    val uri: Uri,
    val displayName: String,
    val artist: String,
    val duration: Long,
    val title: String,
    val imagePath: String? = null,
    val lyrics: String? = null,
    val album: String? = null,
)