package ir.flyap.music_a.feature.home

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.Settings
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
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ir.flyap.music_a.ui.theme.ExtraSmallSpacer
import ir.flyap.music_a.ui.theme.SmallSpacer
import ir.flyap.music_a.ui.theme.dimension
import ir.flyap.music_a.utill.rememberPermissionState

@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    val homeState by homeViewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    val permissionState = rememberPermissionState(
        onGranted = {},
        onDenied = {}
    )

    LaunchedEffect(key1 = Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                val uri = Uri.parse("package:ir.flyap.music_a")
                (context as Activity).startActivity(Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION, uri))
            }
        }
    }

    Scaffold(
        topBar = {
            var artist by remember { mutableStateOf("") }
            Column (modifier = Modifier.padding(dimension.medium)){
                TextField(
                    value = artist,
                    onValueChange = { artist = it },
                    modifier = Modifier.fillMaxWidth()
                )
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(dimension.small), horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    SmallSpacer()
                    Button(
                        onClick = { homeViewModel.mainCrawl(artist) },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(text = "Crawl")
                    }

                    SmallSpacer()

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
                ExtraSmallSpacer()
                Button(
                    onClick = {
                        homeViewModel.itemCrawl(homeState.timeouts, artist)
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Check Timeouts")
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
