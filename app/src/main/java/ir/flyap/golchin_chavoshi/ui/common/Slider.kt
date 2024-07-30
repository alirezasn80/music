import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import ir.flyap.golchin_chavoshi.ui.theme.ExtraSmallSpacer
import ir.flyap.golchin_chavoshi.ui.theme.dimension
import ir.flyap.golchin_chavoshi.utill.createImageBitmap

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SliderImage() {
    val context = LocalContext.current
  val imageList = remember {
      context.assets.list("slider")!!.toList().map { createImageBitmap(context, "slider/$it") }
  }
    val count = imageList.size
    val state = rememberPagerState(initialPage = 0, initialPageOffsetFraction = 0f) { imageList.size }
    val slideImage = remember { mutableStateOf(imageList[0]) }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {

        HorizontalPager(
            state = state,
            reverseLayout = true,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(horizontal = dimension.medium)
                .clip(MaterialTheme.shapes.small),
        ) { page ->
            slideImage.value = imageList[page]

            Image(
                modifier = Modifier
                    .fillMaxSize(),
                bitmap = slideImage.value,
                contentDescription = null,
                contentScale = ContentScale.FillBounds
            )

        }
        ExtraSmallSpacer()

        DotsIndicator(
            totalDots = count,
            selectedIndex = state.currentPage,
            selectedColor = Color(0xFFA0A4B2),
            unSelectedColor = MaterialTheme.colorScheme.secondaryContainer
        )

    }

}

@Composable
private fun DotsIndicator(
    totalDots: Int,
    selectedIndex: Int,
    selectedColor: Color,
    unSelectedColor: Color,
) {

    LazyRow(
        modifier = Modifier
            .wrapContentWidth()
            .wrapContentHeight()
            .padding(8.dp),
        reverseLayout = true
    ) {

        items(totalDots) { index ->
            if (index == selectedIndex) {
                Box(
                    modifier = Modifier
                        .size(5.dp)
                        .clip(CircleShape)
                        .background(selectedColor)
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(5.dp)
                        .clip(CircleShape)
                        .background(unSelectedColor)
                )
            }

            if (index != totalDots - 1) {
                Spacer(modifier = Modifier.padding(horizontal = 2.dp))
            }
        }
    }
}
