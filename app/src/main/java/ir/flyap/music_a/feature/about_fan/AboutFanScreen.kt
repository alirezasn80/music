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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import ir.flyap.music_a.R
import ir.flyap.music_a.ui.theme.MediumSpacer
import ir.flyap.music_a.ui.theme.SmallSpacer
import ir.flyap.music_a.ui.theme.dimension

@Composable
fun AboutFanScreen(upPress: () -> Unit) {
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

            Image(
                painter = painterResource(id = R.drawable.img_logo),
                contentDescription = null,
                modifier = Modifier
                    .clip(CircleShape)
                    .size(100.dp)
            )
            SmallSpacer()
            Text(text = "چغوت نارنگی", style = MaterialTheme.typography.titleSmall)
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
                Text(text = "یه متن چرت پرت که هییج ارزش خوندن نداره. و الان تویی که داری میخونی داری الکی وقتتو برای خواندن این متن هدر میدی. هیچ وقت دیگه این کار رو نکن و همین الان ادامه نده. دیگه این متن بی ارزش تستی رو نخون. منم مجبور بودم برا تست این متن رو بنویسم. پس تو نخون. فقط متن رو ببین. همین. فقط دیدن بدون مفهوم. این متن قراره اطلاعات یه فن پیج نمایش داده بشه. شاید اطلاعات تو باشه. که اون موقع این اطلاعات با ارزش میشه. چون تویی که داری این متن رو میخونی با ارزشی .")
            }
            MediumSpacer()
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = dimension.medium), horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    Modifier
                        .background(MaterialTheme.colorScheme.secondary, MaterialTheme.shapes.medium)
                        .padding(dimension.medium),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(text = "telegram")
                    SmallSpacer()
                    Icon(imageVector = Icons.Rounded.Home, contentDescription = null)

                }

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