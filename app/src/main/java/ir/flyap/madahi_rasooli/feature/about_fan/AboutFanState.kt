package ir.flyap.madahi_rasooli.feature.about_fan

import ir.flyap.madahi_rasooli.api.service.FanInfoModel

data class AboutFanState(
    val isLoading: Boolean = true,
    val data: FanInfoModel? = null,
)
