package ir.flyap.banifatemeh.media

import ir.flyap.banifatemeh.model.Music

data class MediaState(
    val musics: List<Music> = emptyList(),
    val categories: List<String> = emptyList(),
    val currentMusic: Music? = null,
    val currentProgress: Float = 0f,
    val currentDuration: Long = 0L,
    val isPlaying:Boolean =false,
    val isLoading:Boolean =false,
)