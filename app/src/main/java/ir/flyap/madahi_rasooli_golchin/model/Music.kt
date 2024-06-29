package ir.flyap.madahi_rasooli_golchin.model

import android.net.Uri

data class Music(
    val id: String,
    val uri: Uri,
    val fileName:String,
    val displayName: String,
    val artist: String,
    val duration: Long,
    val title: String,
    val imagePath: String? = null,
    val lyrics: String? = null,
    val album: String? = null,
)