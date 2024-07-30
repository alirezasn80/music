package ir.flyap.golchin_chavoshi.feature.detail

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
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
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
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
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ir.flyap.golchin_chavoshi.R
import ir.flyap.golchin_chavoshi.media.MediaViewModel
import ir.flyap.golchin_chavoshi.main.navigation.NavigationState
import ir.flyap.golchin_chavoshi.ui.theme.ExtraSmallSpacer
import ir.flyap.golchin_chavoshi.ui.theme.LargeSpacer
import ir.flyap.golchin_chavoshi.ui.theme.MediumSpacer
import ir.flyap.golchin_chavoshi.ui.theme.SmallSpacer
import ir.flyap.golchin_chavoshi.ui.theme.dimension
import ir.flyap.golchin_chavoshi.utill.createImageBitmap
import ir.flyap.golchin_chavoshi.utill.timeStampToDuration


@Composable
fun DetailScreen(
    navigationState: NavigationState,
    mediaViewModel: MediaViewModel,
    detailViewModel: DetailViewModel = hiltViewModel()
) {

    val mediaState by mediaViewModel.state.collectAsStateWithLifecycle()
    val detailState by detailViewModel.state.collectAsStateWithLifecycle()
    var selectedFrame by remember { mutableStateOf("cover") }
    val context = LocalContext.current

    // show Ad
    LaunchedEffect(Unit) {
        detailViewModel.requestStandardAd(context as Activity)
    }

    if (mediaState.currentMusic != null)
        Scaffold(
            topBar = {
                Header(
                    title = mediaState.currentMusic!!.displayName/*+"(${mediaState.currentMusic!!.id})"*/,
                    upPress = navigationState::upPress,
                    selectedFrame = selectedFrame,
                    onItemClick = { selectedFrame = it }
                )
            },
            bottomBar = {
                BottomMediaBar(
                    duration = mediaState.currentMusic!!.duration,
                    currentDuration = mediaState.currentDuration,
                    isAudioPlaying = mediaState.isPlaying,// todo(i change it)
                    progress = mediaState.currentProgress,
                    onProgressChange = { mediaViewModel.seekTo(it) },
                    onStart = { mediaViewModel.playAudio(mediaState.currentMusic!!) },
                    onNextClick = { mediaViewModel.skipToNext() },
                    onPreviousClick = { mediaViewModel.skipToPrevious() }
                )
            }
        ) { scaffoldPadding ->
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(scaffoldPadding)
                    .background(MaterialTheme.colorScheme.background), horizontalAlignment = Alignment.CenterHorizontally
            ) {
                SmallSpacer()
                if (selectedFrame == "cover") {
                    if (mediaState.currentMusic?.imagePath != null)
                        Image(
                            bitmap = createImageBitmap(context, mediaState.currentMusic!!.imagePath!!),
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
                    Box(
                        modifier = Modifier
                            .padding(vertical = dimension.medium)
                            .clip(MaterialTheme.shapes.small)
                            .background(MaterialTheme.colorScheme.surface)
                            .padding(dimension.small)
                            .fillMaxWidth(0.9f)
                            .aspectRatio(1f),
                    ) {
                        Column(
                            Modifier
                                .fillMaxSize()
                                .verticalScroll(rememberScrollState())
                        ) {
                            Text(text = mediaState.currentMusic!!.lyrics ?: "متنی وجود ندارد!", fontSize = detailState.fontSize)
                        }

                        TextSize(
                            detailState.fontSize
                        ) {
                            detailViewModel.setFontSizeValue(it)
                        }
                    }

                }

                StandardAd(detailViewModel::updateContainer)
            }
        }
}

@Composable
fun BoxScope.TextSize(size: TextUnit, onSize: (TextUnit) -> Unit) {
    Column(
        Modifier
            .clip(MaterialTheme.shapes.medium)
            .background(Color.White.copy(alpha = 0.3f))
            .align(Alignment.BottomEnd),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(imageVector = Icons.Rounded.Add, contentDescription = null, modifier = Modifier.clickable {
            onSize(TextUnit(size.value + 1f, TextUnitType.Sp))
        })
        SmallSpacer()
        Text(text = size.value.toInt().toString())
        SmallSpacer()
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_remove),
            contentDescription = null,
            modifier = Modifier.clickable {
                onSize(TextUnit(size.value - 1f, TextUnitType.Sp))
            }
        )
    }
}

@Composable
private fun StandardAd(
    onUpdate: (ViewGroup) -> Unit,
) {
    val context = LocalContext.current as Activity

    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
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
    val interactionSource = remember { MutableInteractionSource() }
    val isDragging by interactionSource.collectIsDraggedAsState()
    var currentProgress by remember { mutableFloatStateOf(0f) }
    LaunchedEffect(progress) {
        if (!isDragging)
            currentProgress = progress
    }

    Column(
        Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(dimension.medium),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column {
            Slider(
                value = currentProgress,
                onValueChange = {
                    currentProgress = it
                },
                valueRange = 0f..100f,
                onValueChangeFinished = {
                    onProgressChange.invoke(currentProgress)
                },
                interactionSource = interactionSource
            )
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = timeStampToDuration(currentDuration))
                Text(text = timeStampToDuration(duration))
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
            .background(MaterialTheme.colorScheme.background)
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
                        if (selectedFrame == "cover") MaterialTheme.colorScheme.primary
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
                        if (selectedFrame == "lyrics") MaterialTheme.colorScheme.primary
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