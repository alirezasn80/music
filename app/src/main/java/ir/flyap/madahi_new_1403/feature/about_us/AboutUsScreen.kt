package ir.flyap.madahi_new_1403.feature.about_us

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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import ir.flyap.madahi_new_1403.R
import ir.flyap.madahi_new_1403.ui.theme.ExtraSmallSpacer
import ir.flyap.madahi_new_1403.ui.theme.MediumSpacer
import ir.flyap.madahi_new_1403.ui.theme.SmallSpacer
import ir.flyap.madahi_new_1403.ui.theme.dimension
import ir.flyap.madahi_new_1403.utill.openBrowser

@Composable
fun AboutUsScreen(upPress: () -> Unit) {
    val context = LocalContext.current
    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(dimension.medium)
            .verticalScroll(rememberScrollState())
    ) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {

            Icon(
                imageVector = Icons.Rounded.ArrowForward,
                contentDescription = null,
                modifier = Modifier.clickable { upPress() },
                tint = MaterialTheme.colorScheme.onBackground
            )
            SmallSpacer()
            Text(text = "درباره ما", color = MaterialTheme.colorScheme.onBackground)

        }
        SmallSpacer()
        Text(
            text = stringResource(id = R.string.about_us_text),
            color = MaterialTheme.colorScheme.onBackground,
            letterSpacing = 2.sp,
            lineHeight = 28.sp,
        )
        MediumSpacer()
        Text(
            text = "آدرس وب سایت شرکت : flyap.ir",
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.clickable { context.openBrowser("https://www.flyap.ir") }
        )
        MediumSpacer()
        Text(text = "منبع موزیک ها", color = MaterialTheme.colorScheme.onBackground)
        ExtraSmallSpacer()
        Text(text = "موزیک فا", color = MaterialTheme.colorScheme.onBackground, style = MaterialTheme.typography.labelSmall)
    }
}