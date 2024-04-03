package ir.flyap.music_a.feature.splash

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import ir.flyap.music_a.main.navigation.NavigationState
import kotlinx.coroutines.delay


@Composable
fun SplashScreen(navigationState: NavigationState, viewModel: SplashViewModel = hiltViewModel()) {

    LaunchedEffect(key1 = Unit) {
        delay(1000)
        navigationState.navToHome()
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Splash")
    }
}