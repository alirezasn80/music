package ir.flyap.music_a.feature.home

import ir.flyap.music_a.model.Audio

data class HomeState(
    val audios: List<Audio> = emptyList(),
    val currentAudio: Audio? = null,
    val temp: String = "",
    val currentProgress: Float = 0f,
)