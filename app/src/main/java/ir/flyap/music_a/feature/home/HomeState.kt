package ir.flyap.music_a.feature.home

import ir.flyap.music_a.api.service.FanModel


sealed interface HomeDialogKey {
    data object AskRate : HomeDialogKey
    data object BadRate : HomeDialogKey
    data object Hide : HomeDialogKey
}


data class HomeState(
    val showComment: Boolean = true,
    val openAppCount: Int = 0,
    val dialogKey: HomeDialogKey = HomeDialogKey.Hide,
    val needUpdate: Boolean = false,
    val showNotificationAlert: Boolean = true,
    val fans: FanModel? = null,
    val crawlLog: String = "",
    val saveLog: String = "",
    val myMusics :List<HomeViewModel.MyMusic> = emptyList(),
    val timeouts:List<String> = emptyList()
)