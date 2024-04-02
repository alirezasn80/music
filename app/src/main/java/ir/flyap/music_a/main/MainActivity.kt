package ir.flyap.music_a.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import dagger.hilt.android.AndroidEntryPoint
import ir.flyap.music_a.feature.splash.SplashScreen
import ir.flyap.music_a.main.navigation.Screen
import ir.flyap.music_a.main.navigation.rememberNavigationState
import ir.flyap.music_a.ui.theme.MusicTheme
import ir.flyap.music_a.utill.LocaleUtils

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, true)
        LocaleUtils.updateResources(this)
        setContent {
            val navigationState = rememberNavigationState()

            MusicTheme {
                NavHost(
                    navController = navigationState.navController,
                    startDestination = Screen.Splash.route,
                ) {

                    composable(Screen.Splash.route) {
                        SplashScreen(navigationState = navigationState)
                    }

                    composable(Screen.Home.route) {

                    }

                }
            }
        }
    }
}