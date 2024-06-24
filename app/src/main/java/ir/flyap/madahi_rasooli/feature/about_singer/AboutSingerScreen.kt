package ir.flyap.madahi_rasooli.feature.about_singer

import SliderImage
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp

import ir.flyap.madahi_rasooli.ui.theme.MediumSpacer
import ir.flyap.madahi_rasooli.ui.theme.SmallSpacer
import ir.flyap.madahi_rasooli.ui.theme.dimension
import ir.flyap.madahi_rasooli.R

@Composable
fun AboutSingerScreen(upPress: () -> Unit) {
    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
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
            Text(text = "درباره خواننده", color = MaterialTheme.colorScheme.onBackground)

        }
        SmallSpacer()
        SliderImage()
        MediumSpacer()
        Column(
            Modifier
                .padding(dimension.medium)
        ) {
            Text(
                text = stringResource(id = R.string.about_singer_full_text),
                letterSpacing = 2.sp,
                lineHeight = 28.sp,
                color = MaterialTheme.colorScheme.onBackground
            )
            MediumSpacer()
            Text(
                text = "دانلود تصاویر زمینه بزودی ♥\uFE0F",
                color = MaterialTheme.colorScheme.primary,
            )
        }
    }
}