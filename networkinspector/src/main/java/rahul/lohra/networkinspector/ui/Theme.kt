package rahul.lohra.networkinspector.ui

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val NetworkMonitorColorPurple80 = Color(0xFFD0BCFF)
val NetworkMonitorColorPurpleGrey80 = Color(0xFFCCC2DC)
val NetworkMonitorColorPink80 = Color(0xFFEFB8C8)

val NetworkMonitorColorPurple40 = Color(0xFF6650a4)
val NetworkMonitorColorPurpleGrey40 = Color(0xFF625b71)
val NetworkMonitorColorPink40 = Color(0xFF7D5260)

private val NetworkMonitorColorDarkColorScheme = darkColorScheme(
    primary = NetworkMonitorColorPurple80,
    secondary = NetworkMonitorColorPurpleGrey80,
    tertiary = NetworkMonitorColorPink80
)

private val NetworkMonitorColorLightColorScheme = lightColorScheme(
    primary = NetworkMonitorColorPurple40,
    secondary = NetworkMonitorColorPurpleGrey40,
    tertiary = NetworkMonitorColorPink40
)

@Composable
fun MyMonitorTheme(content: @Composable () -> Unit) {
    val darkTheme = isSystemInDarkTheme()
    val colorScheme = when {
         Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> NetworkMonitorColorDarkColorScheme
        else -> NetworkMonitorColorLightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = NetworkMonitorColorTypography,
        content = content
    )
}

// Set of Material typography styles to start with
val NetworkMonitorColorTypography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
)