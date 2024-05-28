package ir.flyap.music_a.feature.about_fan

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowForward
import androidx.compose.material.icons.rounded.Face
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.annotation.ExperimentalCoilApi
import ir.flyap.music_a.R
import ir.flyap.music_a.ui.theme.MediumSpacer
import ir.flyap.music_a.ui.theme.SmallSpacer
import ir.flyap.music_a.ui.theme.dimension
import ir.flyap.music_a.utill.CoilImage

@OptIn(ExperimentalCoilApi::class)
@Composable
fun AboutFanScreen(upPress: () -> Unit, viewModel: AboutFanViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    if (state.isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        if (state.data == null) return
        Column(
            Modifier
                .fillMaxSize(),
        ) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(dimension.medium), verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(imageVector = Icons.Rounded.ArrowForward, contentDescription = null, modifier = Modifier.clickable { upPress() })
                SmallSpacer()
                Text(text = "درباره خواننده")
            }
            Column(
                Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                MediumSpacer()

                CoilImage(
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(100.dp), data = state.data!!.profile
                )
                SmallSpacer()
                Text(text = state.data!!.name, style = MaterialTheme.typography.titleSmall)
                MediumSpacer()
                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(dimension.medium)
                        .background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.8f), MaterialTheme.shapes.small)
                        .padding(dimension.medium)
                ) {
                    Text(text = "توضیحات", style = MaterialTheme.typography.titleSmall)
                    SmallSpacer()
                    Text(text = state.data!!.description)
                }
                MediumSpacer()
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = dimension.medium), horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    if (state.data!!.telegram != null)
                        Row(
                            Modifier
                                .background(MaterialTheme.colorScheme.secondary, MaterialTheme.shapes.medium)
                                .padding(dimension.medium),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = state.data!!.telegram!!)
                            SmallSpacer()
                            Icon(imageVector = Icons.Rounded.Home, contentDescription = null)
                        } else
                        SmallSpacer()

                    Row(
                        Modifier
                            .background(MaterialTheme.colorScheme.secondary, MaterialTheme.shapes.medium)

                            .padding(dimension.medium),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "instagram")
                        SmallSpacer()
                        Icon(imageVector = Icons.Rounded.Face, contentDescription = null)
                    }
                }

            }
        }
    }
}