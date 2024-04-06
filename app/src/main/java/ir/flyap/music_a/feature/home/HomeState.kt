package ir.flyap.music_a.feature.home

import ir.flyap.music_a.model.Audio

data class HomeState(
    val audios: List<Audio> = emptyList(),
    val currentAudio: Audio? = null,
    val currentProgress: Float = 0f,
    val currentDuration: Long = 0L
)