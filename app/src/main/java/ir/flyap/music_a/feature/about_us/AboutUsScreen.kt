package ir.flyap.music_a.feature.about_us

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import ir.flyap.music_a.R
import ir.flyap.music_a.ui.theme.ExtraSmallSpacer
import ir.flyap.music_a.ui.theme.MediumSpacer
import ir.flyap.music_a.ui.theme.dimension
import ir.flyap.music_a.utill.openBrowser

@Composable
fun AboutUsScreen(upPress: () -> Unit) {
    val context = LocalContext.current
    Column(
        Modifier
            .fillMaxSize()
            .padding(dimension.medium)
            .verticalScroll(rememberScrollState())
    ) {
        Text(text = stringResource(id = R.string.about_us_text))
        MediumSpacer()
        TextButton(onClick = { context.openBrowser("https://www.flyap.ir") }) {
            Text(text = "https://www.flyap.ir", color = MaterialTheme.colorScheme.primary)
        }
        MediumSpacer()
        Text(text = "منابع استفاده شده:")
        ExtraSmallSpacer()
        Text(text = "https://nicmusic.net/")
    }
}