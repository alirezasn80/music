package ir.flyap.golchin_chavoshi.feature.about_fan

import ir.flyap.golchin_chavoshi.api.service.FanInfoModel

data class AboutFanState(
    val isLoading: Boolean = true,
    val data: FanInfoModel? = null,
)
