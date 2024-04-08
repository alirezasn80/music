package ir.flyap.music_a.main

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import dagger.hilt.android.AndroidEntryPoint
import ir.flyap.music_a.feature.detail.DetailScreen
import ir.flyap.music_a.feature.home.HomeScreen
import ir.flyap.music_a.feature.home.HomeViewModel
import ir.flyap.music_a.feature.splash.SplashScreen
import ir.flyap.music_a.main.navigation.Screen
import ir.flyap.music_a.main.navigation.rememberNavigationState
import ir.flyap.music_a.ui.theme.MusicTheme
import ir.flyap.music_a.utill.LocaleUtils
import ir.flyap.music_a.utill.debug
import ir.tapsell.plus.TapsellPlus
import ir.tapsell.plus.TapsellPlusInitListener
import ir.tapsell.plus.model.AdNetworkError
import ir.tapsell.plus.model.AdNetworks


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, true)
        LocaleUtils.updateResources(this)
        initTapsell(this)
        setContent {
            val navigationState = rememberNavigationState()
            val mediaViewModel: HomeViewModel = hiltViewModel()


            MusicTheme {
                NavHost(
                    navController = navigationState.navController,
                    startDestination = Screen.Splash.route,
                ) {

                    composable(Screen.Splash.route) {
                        SplashScreen(navigationState = navigationState)
                    }

                    composable(Screen.Home.route) {
                        HomeScreen(
                            navigationState = navigationState,
                            viewModel = mediaViewModel
                        )
                    }

                    composable(Screen.Detail.route) {
                        DetailScreen(
                            navigationState = navigationState,
                            viewModel = mediaViewModel
                        )
                    }

                }
            }
        }
    }

    private fun initTapsell(context: Context) {
        TapsellPlus.initialize(this, "TAPSELL_KEY",
            object : TapsellPlusInitListener {
                override fun onInitializeSuccess(adNetworks: AdNetworks) {
                    debug("success init tapsell :  ${adNetworks.name}")
                    TapsellPlus.setGDPRConsent(context, true)
                }

                override fun onInitializeFailed(
                    adNetworks: AdNetworks,
                    adNetworkError: AdNetworkError
                ) {
                    debug("failed init tapsell :  adNetworks.name ${adNetworks.name} : ${adNetworkError.errorMessage}")

                }
            })
    }
}