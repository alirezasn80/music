package ir.flyap.music_a.feature.home

import ir.flyap.music_a.model.Music

data class HomeState(
    val musics: List<Music> = emptyList(),
    val categories: List<String> = emptyList(),
    val currentMusic: Music? = null,
    val currentProgress: Float = 0f,
    val currentDuration: Long = 0L,
    val isPlaying:Boolean =false,
)