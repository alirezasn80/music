package ir.flyap.madahi_rasooli_golchin.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


@Composable
fun BlurLayout() {
    Box(
        Modifier
            .fillMaxSize()
            .background(Color.Black.copy(0.75f))
            .blur(0.dp, edgeTreatment = BlurredEdgeTreatment.Unbounded)
            .clickable(enabled = false) {}
            .shadow(1000.dp),
    )
}
