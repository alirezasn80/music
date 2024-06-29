package ir.flyap.banifatemeh.feature.about_fan

import ir.flyap.banifatemeh.api.service.FanInfoModel

data class AboutFanState(
    val isLoading: Boolean = true,
    val data: FanInfoModel? = null,
)
