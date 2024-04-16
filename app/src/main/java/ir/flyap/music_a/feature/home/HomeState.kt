package ir.flyap.music_a.feature.home


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
    val showNotificationAlert: Boolean = true
)