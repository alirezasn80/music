package ir.flyap.chavoshi.feature.about_fan

import ir.flyap.chavoshi.api.service.FanInfoModel

data class AboutFanState(
    val isLoading: Boolean = true,
    val data: FanInfoModel? = null,
)
