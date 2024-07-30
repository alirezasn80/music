package ir.flyap.chavoshi.main

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import dagger.hilt.android.AndroidEntryPoint
import ir.flyap.chavoshi.feature.about_fan.AboutFanScreen
import ir.flyap.chavoshi.feature.about_singer.AboutSingerScreen
import ir.flyap.chavoshi.feature.about_us.AboutUsScreen
import ir.flyap.chavoshi.feature.detail.DetailScreen
import ir.flyap.chavoshi.feature.home.HomeScreen
import ir.flyap.chavoshi.media.MediaViewModel
import ir.flyap.chavoshi.feature.splash.SplashScreen
import ir.flyap.chavoshi.main.navigation.Screen
import ir.flyap.chavoshi.main.navigation.rememberNavigationState
import ir.flyap.chavoshi.ui.theme.MusicTheme
import ir.flyap.chavoshi.utill.LocaleUtils


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private var mediaViewModel: MediaViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, true)
        LocaleUtils.updateResources(this)

        if (intent.hasExtra("url")) {
            val url = intent.getStringExtra("url")
            val i = Intent(Intent.ACTION_VIEW)
            i.setData(Uri.parse(url))
            startActivity(i)
        }

        setContent {
            val navigationState = rememberNavigationState()
            mediaViewModel = hiltViewModel()


            MusicTheme {
                NavHost(
                    navController = navigationState.navController,
                    startDestination = Screen.Home.route,
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

                    composable(Screen.AboutSinger.route) {
                        AboutSingerScreen(navigationState::upPress)
                    }

                    composable(Screen.AboutFan.route) {
                        AboutFanScreen(navigationState::upPress)
                    }

                    composable(Screen.AboutUs.route) {
                        AboutUsScreen(navigationState::upPress)
                    }

                }
            }
        }
    }

    override fun onResume() {
        super.onResume()

    }

    override fun onStop() {
        super.onStop()

    }

    override fun onPause() {
        super.onPause()

    }

    override fun onStart() {
        super.onStart()

    }

    override fun onDestroy() {
        super.onDestroy()

    }

}