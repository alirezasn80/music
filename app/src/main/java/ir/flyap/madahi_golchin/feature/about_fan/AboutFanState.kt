package ir.flyap.madahi_golchin.feature.about_fan

import ir.flyap.madahi_golchin.api.service.FanInfoModel

data class AboutFanState(
    val isLoading: Boolean = true,
    val data: FanInfoModel? = null,
)
