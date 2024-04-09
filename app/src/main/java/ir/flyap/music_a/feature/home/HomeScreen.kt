package ir.flyap.music_a.feature.home

import SliderImage
import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ir.flyap.music_a.R
import ir.flyap.music_a.main.navigation.NavigationState
import ir.flyap.music_a.media.MediaViewModel
import ir.flyap.music_a.model.Music
import ir.flyap.music_a.ui.theme.SmallSpacer
import ir.flyap.music_a.ui.theme.dimension
import ir.flyap.music_a.utill.createImageBitmap
import ir.flyap.music_a.utill.shareFile
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    navigationState: NavigationState,
    mediaViewModel: MediaViewModel,
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    val state by mediaViewModel.state.collectAsStateWithLifecycle()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val activity = LocalContext.current as Activity

    DisposableEffect(Unit) {
        onDispose {
            // viewModel.destroyAd(context)
        }
    }

    // show Ad
    LaunchedEffect(Unit) {
        homeViewModel.requestStandardAd(activity)
        homeViewModel.requestInterstitialAd(activity)
    }

    // Close drawer
    BackHandler(drawerState.isOpen) {
        if (drawerState.isOpen)
            scope.launch { drawerState.close() }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerShape = RectangleShape,
                drawerContainerColor = MaterialTheme.colorScheme.background,
            ) {
                DrawerItem(label = R.string.register, icon = ImageVector.vectorResource(R.drawable.ic_register), onClick = {})

                DrawerItem(
                    label = R.string.about_us,
                    icon = ImageVector.vectorResource(R.drawable.ic_about),
                    onClick = {}
                )
            }
        }
    ) {
        Scaffold(
            bottomBar = {
                state.currentMusic?.let { currentPlayingAudio ->
                    BottomBarPlayer(
                        music = currentPlayingAudio,
                        isAudioPlaying = mediaViewModel.isPlaying,
                        onStart = { mediaViewModel.playAudio(currentPlayingAudio) },
                        onNext = { mediaViewModel.skipToNext() },
                        onclick = { homeViewModel.showInterstitialAd(activity, navigationState::navToDetail) }
                    )
                }
            },
            topBar = {
                Box(
                    Modifier.fillMaxWidth(),
                ) {

                    IconButton(onClick = { scope.launch { drawerState.open() } }) {
                        Icon(imageVector = ImageVector.vectorResource(R.drawable.ic_menu), contentDescription = null)
                    }

                    Text(
                        text = "آهنگ های مشت", style = MaterialTheme.typography.titleSmall, modifier = Modifier.align(Alignment.Center)
                    )
                }

            }
        ) { paddingValues ->

            Column(
                Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                SmallSpacer()
                SliderImage()
                AlbumBar(
                    items = state.categories,
                    onAlbumClick = mediaViewModel::onAlbumClick
                )
                SmallSpacer()
                PlayAll(onClick = { mediaViewModel.playAudio(state.musics[0]) })
                StandardAd(
                    onUpdate = homeViewModel::updateStandardBannerContainer
                )
                SmallSpacer()
                LazyColumn {
                    items(state.musics) { audio ->
                        AudioItem(
                            music = audio,
                            onItemClick = {
                                if (!mediaViewModel.isPlaying) mediaViewModel.playAudio(audio)
                                homeViewModel.showInterstitialAd(activity, navigationState::navToDetail)
                            },
                            onSaveFileClick = {}
                        )


                    }
                }
            }

        }
    }


}

@Composable
private fun StandardAd(
    onUpdate: (ViewGroup) -> Unit = {},
) {
    val context = LocalContext.current as Activity
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        AndroidView(
            modifier = Modifier,
            factory = {
                val view =
                    LayoutInflater.from(context)
                        .inflate(R.layout.standard_banner_container, null, false)
                val frameLayout = view.findViewById<ViewGroup>(R.id.standardBanner)
                frameLayout
            },
            update = onUpdate
        )
    }

}


@Composable
private fun PlayAll(onClick: () -> Unit) {

    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(dimension.medium)) {
        IconButton(
            onClick = onClick,
            modifier = Modifier
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary)
                .size(30.dp)
        ) {
            Icon(
                imageVector = Icons.Rounded.PlayArrow, contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimary,
            )
        }
        SmallSpacer()
        Text(text = stringResource(id = R.string.play_all))
    }


}

@Composable
private fun AlbumBar(
    items: List<String>,
    onAlbumClick: (String) -> Unit
) {
    if (items.isNotEmpty())
        Row(
            Modifier
                .padding(horizontal = dimension.medium),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = stringResource(id = R.string.album))
            SmallSpacer()
            Row(Modifier.fillMaxWidth()) {
                var selectedAlbum by remember { mutableStateOf(items[0]) }
                items.forEach {
                    AlbumChip(
                        title = it,
                        selectedAlbum == it,
                        onClick = {
                            if (selectedAlbum != it) {
                                selectedAlbum = it
                                onAlbumClick(it)
                            }

                        }
                    )
                }
            }
        }
}

@Composable
fun AlbumChip(title: String, isSelected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .padding(horizontal = dimension.extraSmall)
            .clip(MaterialTheme.shapes.small)
            .clickable { onClick() }
            .background(if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primary.copy(alpha = 0.4f), MaterialTheme.shapes.small)
            .padding(horizontal = dimension.medium, vertical = dimension.extraSmall),
        contentAlignment = Alignment.Center
    )
    {
        Text(text = title)
    }
}

@Composable
private fun AudioItem(
    music: Music,
    onItemClick: () -> Unit,
    onSaveFileClick: () -> Unit
) {
    val context = LocalContext.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = dimension.small, vertical = dimension.extraSmall)
            .background(MaterialTheme.colorScheme.surface, MaterialTheme.shapes.small)
            .clickable {
                onItemClick.invoke()
            }
            .padding(vertical = dimension.small),
        verticalAlignment = Alignment.CenterVertically
    ) {
        SmallSpacer()
        if (music.imagePath != null)
            Image(
                bitmap = createImageBitmap(context, music.imagePath),
                contentDescription = null,
                modifier = Modifier
                    .clip(MaterialTheme.shapes.small)
                    .size(50.dp)
            )
        else
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_background),
                contentDescription = null,
                modifier = Modifier
                    .clip(MaterialTheme.shapes.small)
                    .size(50.dp)
            )
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = dimension.small)
        ) {
            Text(
                text = music.displayName,
                style = MaterialTheme.typography.titleSmall,
                overflow = TextOverflow.Clip,
                maxLines = 1
            )
            /*ExtraSmallSpacer()
            Text(
                text = audio.artist,
                style = MaterialTheme.typography.labelSmall,
                maxLines = 1,
                overflow = TextOverflow.Clip,
                color = MaterialTheme.colorScheme
                    .onSurface
                    .copy(alpha = .5f)
            )*/

        }
        SmallSpacer()
        IconButton(onClick = { context.shareFile(music) }) {
            Icon(imageVector = Icons.Rounded.Share, contentDescription = null)
        }

        /*PopUpMenu(
            mainIcon = Icons.Rounded.MoreVert,
            titleMenuItems = listOf("اشتراک گذاری", "ذخیره"),
            iconMenuItems = listOf(Icons.Rounded.Share, ImageVector.vectorResource(R.drawable.ic_save))
        ) {
            when (it) {

                0 -> {
                    context.shareFile(audio)
                }

                1 -> {
                    onSaveFileClick()
                }
            }
        }*/

    }
}

@Composable
private fun RowScope.ArtistInfo(
    music: Music
) {
    Row(
        modifier = Modifier.weight(1f),
        verticalAlignment = Alignment.CenterVertically,
    ) {

        Column {
            Text(
                text = music.displayName,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleSmall,
                overflow = TextOverflow.Clip,
                maxLines = 1,
                color = MaterialTheme.colorScheme.onSecondary
            )

            /*Text(
                text = audio.artist,
                fontWeight = FontWeight.Normal,
                style = MaterialTheme.typography.labelSmall,
                overflow = TextOverflow.Clip,
                maxLines = 1,
                color = MaterialTheme.colorScheme.onSecondary

            )*/

        }


    }


}

@Composable
private fun PlayStopButton(
    icon: ImageVector,
    border: BorderStroke? = null,
    backgroundColor: Color = MaterialTheme.colorScheme.surface,
    color: Color = MaterialTheme.colorScheme.onSurface,
    onClick: () -> Unit
) {

    Surface(
        shape = CircleShape,
        border = border,
        modifier = Modifier
            .size(30.dp)
            .clip(CircleShape)
            .clickable {
                onClick.invoke()
            },
        contentColor = color,
        color = backgroundColor

    ) {
        Box(
            modifier = Modifier.padding(4.dp),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = MaterialTheme.colorScheme.onSecondary
            )

        }


    }


}

@Composable
private fun MediaPlayerController(
    isAudioPlaying: Boolean,
    onStart: () -> Unit,
    onNext: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {

        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_skip_next),
            contentDescription = null,
            modifier = Modifier
                .clickable { onNext.invoke() }
                .size(15.dp),
            tint = MaterialTheme.colorScheme.onSecondary
        )

        SmallSpacer()

        PlayStopButton(
            icon = if (isAudioPlaying) ImageVector.vectorResource(R.drawable.ic_stop)
            else ImageVector.vectorResource(R.drawable.ic_play),
            backgroundColor = MaterialTheme.colorScheme.primary
        ) {
            onStart.invoke()
        }

    }
}

@Composable
private fun BottomBarPlayer(
    onclick: () -> Unit,
    music: Music,
    isAudioPlaying: Boolean,
    onStart: () -> Unit,
    onNext: () -> Unit
) {
    Row(
        modifier = Modifier
            .padding(dimension.small)
            .clip(MaterialTheme.shapes.medium)
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.secondary, RoundedCornerShape(topEnd = 5.dp, topStart = 5.dp))
            .clickable { onclick() }
            .padding(horizontal = dimension.medium, vertical = dimension.medium),
        verticalAlignment = Alignment.CenterVertically
    ) {

        MediaPlayerController(
            isAudioPlaying = isAudioPlaying,
            onStart = { onStart.invoke() },
            onNext = { onNext.invoke() }
        )

        SmallSpacer()

        ArtistInfo(music = music)
    }

    /*Slider(
        value = progress,
        onValueChange = { onProgressChange.invoke(it) },
        valueRange = 0f..100f
    )*/


}

@Composable
private fun DrawerItem(
    label: Int,
    icon: Any,
    color: Color = Color.Unspecified,
    onClick: () -> Unit
) {
    Row(
        Modifier
            .clickable { onClick() }
            .fillMaxWidth()
            .padding(dimension.medium),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (icon is ImageVector)
            Icon(imageVector = icon, contentDescription = null, modifier = Modifier.size(24.dp), tint = color)
        else if (icon is Painter)
            Image(painter = icon, contentDescription = null, modifier = Modifier.size(24.dp), contentScale = ContentScale.Fit)
        SmallSpacer()
        Text(text = stringResource(id = label), maxLines = 1, overflow = TextOverflow.Ellipsis, style = MaterialTheme.typography.titleSmall)


    }
}