package ir.flyap.madahi_new_1403.feature.about_fan

import ir.flyap.madahi_new_1403.api.service.FanInfoModel

data class AboutFanState(
    val isLoading: Boolean = true,
    val data: FanInfoModel? = null,
)
