package ir.flyap.music_a.feature.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ir.flyap.music_a.main.navigation.NavigationState
import ir.flyap.music_a.media.MediaViewModel
import ir.flyap.music_a.ui.theme.dimension
import ir.flyap.music_a.utill.rememberPermissionState

@Composable
fun HomeScreen(
    navigationState: NavigationState,
    mediaViewModel: MediaViewModel,
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    val homeState by homeViewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    val permissionState = rememberPermissionState(
        onGranted = {},
        onDenied = {}
    )

    Scaffold(
        topBar = {
            Column {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Button(
                        onClick = { homeViewModel.crawl() },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(text = "Crawl")
                    }

                    Button(
                        onClick = {
                            homeViewModel.downloadFiles(
                                context = context,
                                items = homeState.myMusics
                            )
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(text = "Save")
                    }
                }

            }

        }
    ) { paddingValues ->
        Column(
            Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(dimension.medium)
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = homeState.crawlLog,
                style = MaterialTheme.typography.bodyLarge.copy(textDirection = TextDirection.Ltr)
            )

            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.dp)
            )

            Text(
                modifier = Modifier.fillMaxWidth(),
                text = homeState.saveLog,
                style = MaterialTheme.typography.bodyLarge.copy(textDirection = TextDirection.Ltr)
            )
        }

    }
}
