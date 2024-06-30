package ir.flyap.pooyanfar.feature.about_fan

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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.annotation.ExperimentalCoilApi
import ir.flyap.pooyanfar.R
import ir.flyap.pooyanfar.ui.theme.MediumSpacer
import ir.flyap.pooyanfar.ui.theme.SmallSpacer
import ir.flyap.pooyanfar.ui.theme.dimension
import ir.flyap.pooyanfar.utill.CoilImage
import ir.flyap.pooyanfar.utill.openBrowser

@OptIn(ExperimentalCoilApi::class)
@Composable
fun AboutFanScreen(upPress: () -> Unit, viewModel: AboutFanViewModel = hiltViewModel()) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsStateWithLifecycle()
    if (state.isLoading) {
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        if (state.data == null) return
        Column(
            Modifier
                .background(MaterialTheme.colorScheme.background)
                .fillMaxSize(),
        ) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(dimension.medium), verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Rounded.ArrowForward,
                    contentDescription = null,
                    modifier = Modifier.clickable { upPress() },
                    tint = MaterialTheme.colorScheme.onBackground
                )
                SmallSpacer()
                Text(
                    text = "درباره خواننده",
                    color = MaterialTheme.colorScheme.onBackground
                )
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
                        .size(150.dp), data = state.data!!.profile
                )
                SmallSpacer()
                Text(
                    text = state.data!!.name,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onBackground
                )
                MediumSpacer()
                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(dimension.medium)
                        .background(MaterialTheme.colorScheme.surface, MaterialTheme.shapes.small)
                        .padding(dimension.medium)
                ) {
                    Text(
                        text = "توضیحات",
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    SmallSpacer()
                    Text(
                        text = state.data!!.description,
                        color = MaterialTheme.colorScheme.onSurface
                    )
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
                                .clickable { context.openBrowser("https://t.me/${state.data!!.telegram!!}") }
                                .background(MaterialTheme.colorScheme.surface, MaterialTheme.shapes.medium)
                                .padding(dimension.medium),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = state.data!!.telegram!!,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            SmallSpacer()
                            Icon(
                                imageVector = ImageVector.vectorResource(id = R.drawable.ic_telegram),
                                contentDescription = null,
                                tint = Color.Unspecified,
                                modifier = Modifier.size(24.dp)
                            )
                        } else
                        SmallSpacer()

                    if (state.data!!.instagram != null)
                        Row(
                            Modifier
                                .clickable { context.openBrowser("https://www.instagram.com/${state.data!!.instagram!!}") }
                                .background(MaterialTheme.colorScheme.surface, MaterialTheme.shapes.medium)
                                .padding(dimension.medium),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = state.data!!.instagram!!, color = MaterialTheme.colorScheme.onSurface)
                            SmallSpacer()
                            Icon(
                                imageVector = ImageVector.vectorResource(id = R.drawable.ic_instagram),
                                contentDescription = null,
                                tint = Color.Unspecified,
                                modifier = Modifier.size(30.dp)

                            )
                        }
                }

            }
        }
    }
}