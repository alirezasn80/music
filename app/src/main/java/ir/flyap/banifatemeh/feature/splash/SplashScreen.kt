package ir.flyap.banifatemeh.feature.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ir.flyap.banifatemeh.R
import ir.flyap.banifatemeh.main.navigation.NavigationState
import ir.flyap.banifatemeh.utill.Destination


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
        modifier = Modifier.background(MaterialTheme.colorScheme.background).fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.img_logo),
            contentDescription = null,
            modifier = Modifier.clip(CircleShape).size(200.dp)
        )
    }
}