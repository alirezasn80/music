package ir.flyap.pooyanfar.feature.about_fan

import ir.flyap.pooyanfar.api.service.FanInfoModel

data class AboutFanState(
    val isLoading: Boolean = true,
    val data: FanInfoModel? = null,
)
