package com.educalab.ninobiologo.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColors = lightColorScheme(
    primary = JungleGreen40,
    onPrimary = SurfaceLight,
    primaryContainer = JungleGreen80,
    onPrimaryContainer = JungleGreen20,
    secondary = LabAmber40,
    onSecondary = SurfaceLight,
    secondaryContainer = LabAmber80,
    tertiary = OceanBlue40,
    tertiaryContainer = OceanBlue80,
    error = CoralRed40,
    background = BgLight,
    onBackground = TextLight,
    surface = SurfaceLight,
    onSurface = TextLight,
    surfaceVariant = JungleGreen80,
)

private val DarkColors = darkColorScheme(
    primary = JungleGreen80,
    onPrimary = JungleGreen20,
    primaryContainer = JungleGreen20,
    onPrimaryContainer = JungleGreen80,
    secondary = LabAmber80,
    onSecondary = LabAmber40,
    tertiary = OceanBlue80,
    error = Color(0xFFFFB4AB),
    background = BgDark,
    onBackground = TextDark,
    surface = SurfaceDark,
    onSurface = TextDark,
)

@Composable
fun NinoBiologoTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // se prioriza la identidad visual propia sobre Material You
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColors
        else -> LightColors
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window ?: return@SideEffect
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = NinoBiologoTypography,
        shapes = NinoBiologoShapes,
        content = content
    )
}
