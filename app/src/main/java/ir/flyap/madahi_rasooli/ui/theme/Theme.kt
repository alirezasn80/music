package ir.flyap.madahi_rasooli.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat


private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF88B0D3),
    secondary = BlueGrayDrawer,
    background = BlueGrayBackground,
    onBackground = PurpleOnBackground,
    onPrimary = Color.White,
    onSecondary = Color.White,
    surface = Color(0xFF295077),
    onSurface = PurpleOnBackground
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFFE3B04B),
    secondary = Color(0xFFF1D6AB),
    background = Color(0xFF2B2B28),
    onBackground = Color(0xFFF8F8F8),
    onPrimary = Color(0xFFF8F8F8),
    onSecondary = Color(0xFFF8F8F8),
    surface = Color(0xFF1A1C20),
    onSurface = Color(0xFFF8F8F8)
)


@Composable
fun MusicTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = LightColorScheme
    /*when {
    dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
        val context = LocalContext.current
        if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
    }

    darkTheme -> DarkColorScheme
    else -> LightColorScheme
}*/
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}