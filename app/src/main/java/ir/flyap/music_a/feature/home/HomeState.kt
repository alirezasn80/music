package ir.flyap.music_a.feature.home

import ir.flyap.music_a.R
import kotlin.random.Random


sealed interface HomeDialogKey {
    data object AskRate : HomeDialogKey
    data object BadRate : HomeDialogKey
    data object Hide : HomeDialogKey
}

data class FanModel(
    val img: Int = R.drawable.img_logo,
    val name: String = "ناشناس",
    val id: Int = Random.nextInt(1000)
)


data class HomeState(
    val showComment: Boolean = true,
    val openAppCount: Int = 0,
    val dialogKey: HomeDialogKey = HomeDialogKey.Hide,
    val needUpdate: Boolean = false,
    val showNotificationAlert: Boolean = true,
    val fans: List<FanModel> = listOf(
        FanModel(),
        FanModel(),
        FanModel(),
        FanModel(),
        FanModel(),
        FanModel(),
        FanModel(),
        FanModel(),
        FanModel(),
        FanModel(),
        FanModel(),
    )
)