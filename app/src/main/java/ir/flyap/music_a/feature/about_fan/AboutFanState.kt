package ir.flyap.music_a.feature.about_fan

import ir.flyap.music_a.api.service.FanInfoModel

data class AboutFanState(
    val isLoading: Boolean = true,
    val data: FanInfoModel? = null,
)
