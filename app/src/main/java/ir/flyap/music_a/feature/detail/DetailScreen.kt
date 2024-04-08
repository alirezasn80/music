package ir.flyap.music_a.feature.detail

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ir.flyap.music_a.R
import ir.flyap.music_a.feature.home.HomeViewModel
import ir.flyap.music_a.main.navigation.NavigationState
import ir.flyap.music_a.ui.theme.ExtraSmallSpacer
import ir.flyap.music_a.ui.theme.LargeSpacer
import ir.flyap.music_a.ui.theme.MediumSpacer
import ir.flyap.music_a.ui.theme.SmallSpacer
import ir.flyap.music_a.ui.theme.dimension
import ir.flyap.music_a.utill.createImageBitmap
import ir.flyap.music_a.utill.timeStampToDuration

@Composable
fun DetailScreen(
    navigationState: NavigationState,
    viewModel: HomeViewModel
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    var selectedFrame by remember { mutableStateOf("cover") }
    val context = LocalContext.current

    if (state.currentMusic != null)
        Scaffold(
            topBar = {
                Header(
                    title = state.currentMusic!!.displayName,
                    upPress = navigationState::upPress,
                    selectedFrame = selectedFrame,
                    onItemClick = { selectedFrame = it }
                )
            },
            bottomBar = {
                BottomMediaBar(
                    duration = state.currentMusic!!.duration,
                    currentDuration = state.currentDuration,
                    isAudioPlaying = viewModel.isPlaying,
                    progress = state.currentProgress,
                    onProgressChange = { viewModel.seekTo(it) },
                    onStart = { viewModel.playAudio(state.currentMusic!!) },
                    onNextClick = { viewModel.skipToNext() },
                    onPreviousClick = { viewModel.skipToPrevious() }
                )
            }
        ) { scaffoldPadding ->
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(scaffoldPadding)
                    .background(MaterialTheme.colorScheme.primary), horizontalAlignment = Alignment.CenterHorizontally
            ) {
                SmallSpacer()
                if (selectedFrame == "cover") {
                    if (state.currentMusic?.imagePath != null)
                        Image(
                            bitmap = createImageBitmap(context, state.currentMusic!!.imagePath!!),
                            contentDescription = null,
                            modifier = Modifier
                                .padding(vertical = dimension.medium)
                                .clip(MaterialTheme.shapes.small)
                                .fillMaxWidth(0.9f)
                                .aspectRatio(1f),
                            contentScale = ContentScale.Crop
                        ) else
                        Image(
                            painter = painterResource(id = R.drawable.ic_launcher_background),
                            contentDescription = null,
                            modifier = Modifier
                                .padding(vertical = dimension.medium)
                                .clip(MaterialTheme.shapes.small)
                                .fillMaxWidth(0.9f)
                                .aspectRatio(1f),
                            contentScale = ContentScale.Crop
                        )
                } else {
                    Column(
                        modifier = Modifier
                            .padding(vertical = dimension.medium)
                            .clip(MaterialTheme.shapes.small)
                            .background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f))
                            .padding(dimension.small)
                            .fillMaxWidth(0.9f)
                            .aspectRatio(1f)
                            .verticalScroll(rememberScrollState()),
                    ) {
                        Text(text = state.currentMusic!!.lyrics ?: "متنی وجود ندارد!")
                    }

                }


            }
        }

}

@Composable
fun BottomMediaBar(
    duration: Long,
    currentDuration: Long,
    isAudioPlaying: Boolean,
    progress: Float,
    onProgressChange: (Float) -> Unit,
    onStart: () -> Unit,
    onNextClick: () -> Unit,
    onPreviousClick: () -> Unit,
) {
    Column(
        Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.secondary)
            .padding(dimension.medium),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column {
            Slider(
                value = progress,
                onValueChange = { onProgressChange.invoke(it) },
                valueRange = 0f..100f
            )
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = timeStampToDuration(duration))
                Text(text = timeStampToDuration(currentDuration))
            }
        }

        MediumSpacer()

        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
            ForwardButton(onClick = onNextClick)
            LargeSpacer()
            PlayStopButton(
                icon = if (isAudioPlaying) ImageVector.vectorResource(R.drawable.ic_stop)
                else ImageVector.vectorResource(R.drawable.ic_play),
                backgroundColor = MaterialTheme.colorScheme.primary
            ) {
                onStart.invoke()
            }
            LargeSpacer()
            BackwardButton(onClick = onPreviousClick)
        }

    }
}

@Composable
fun ForwardButton(onClick: () -> Unit) {
    IconButton(onClick = onClick) {
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_skip_next),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier.size(18.dp)
        )
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
            .size(40.dp)
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
                modifier = Modifier.size(40.dp),
                tint = MaterialTheme.colorScheme.onSecondary
            )

        }


    }


}


@Composable
fun BackwardButton(onClick: () -> Unit) {

    IconButton(onClick = onClick) {
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_skip_next),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier
                .rotate(180f)
                .size(18.dp),
        )
    }
}

@Composable
fun Header(
    title: String,
    selectedFrame: String,
    onItemClick: (String) -> Unit,
    upPress: () -> Unit
) {
    Row(
        Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary)
            .padding(vertical = dimension.extraSmall),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            UpPress(upPress)
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }


        Row(verticalAlignment = Alignment.CenterVertically) {
            SmallSpacer()
            IconButton(
                onClick = {
                    onItemClick("cover")

                },
                modifier = Modifier
                    .clip(CircleShape)
                    .background(
                        if (selectedFrame == "cover") MaterialTheme.colorScheme.secondary.copy(alpha = 0.7f)
                        else
                            Color.Unspecified
                    )
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_img),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary

                )
            }
            ExtraSmallSpacer()

            IconButton(
                onClick = {
                    onItemClick("lyrics")

                },
                modifier = Modifier
                    .clip(CircleShape)
                    .background(
                        if (selectedFrame == "lyrics") MaterialTheme.colorScheme.secondary.copy(alpha = 0.7f)
                        else
                            Color.Unspecified
                    )
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_lyrics),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary

                )
            }
            SmallSpacer()
        }
    }
}

@Composable
fun UpPress(onClick: () -> Unit) {
    IconButton(onClick = onClick) {
        Icon(
            imageVector = Icons.Rounded.ArrowForward,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onPrimary
        )
    }
}