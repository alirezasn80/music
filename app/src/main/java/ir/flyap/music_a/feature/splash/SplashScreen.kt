package ir.flyap.music_a.feature.splash

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ir.flyap.music_a.main.navigation.NavigationState
import ir.flyap.music_a.utill.Destination
import kotlinx.coroutines.delay


@Composable
fun SplashScreen(navigationState: NavigationState, viewModel: SplashViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val destination = viewModel.destination

    LaunchedEffect(key1 = destination) {
        when (destination) {
            Destination.Home -> navigationState.navToHome()
            else -> Unit
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Splash")
    }
}