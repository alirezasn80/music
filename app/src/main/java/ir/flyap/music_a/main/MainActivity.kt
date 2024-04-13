package ir.flyap.music_a.main

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
import ir.flyap.music_a.media.MediaViewModel
import ir.flyap.music_a.feature.splash.SplashScreen
import ir.flyap.music_a.main.navigation.Screen
import ir.flyap.music_a.main.navigation.rememberNavigationState
import ir.flyap.music_a.ui.theme.MusicTheme
import ir.flyap.music_a.utill.LocaleUtils
import ir.flyap.music_a.utill.debug


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private  var mediaViewModel: MediaViewModel? =null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, true)
        LocaleUtils.updateResources(this)
        setContent {
            val navigationState = rememberNavigationState()
            mediaViewModel = hiltViewModel()


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
                            mediaViewModel = mediaViewModel!!
                        )
                    }

                    composable(Screen.Detail.route) {
                        DetailScreen(
                            navigationState = navigationState,
                            mediaViewModel = mediaViewModel!!
                        )
                    }

                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        debug("onResume")
    }

    override fun onStop() {
        super.onStop()
        debug("onStop")
    }

    override fun onPause() {
        super.onPause()
        debug("onPause")
    }

    override fun onStart() {
        super.onStart()
        debug("onStart")
    }

    override fun onDestroy() {
        super.onDestroy()
        debug("onDestroy")
    }

}