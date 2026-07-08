package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = JadePrimaryDark,
    onPrimary = Color(0xFF0F1B17),
    primaryContainer = Color(0xFF1E332B),
    onPrimaryContainer = Color(0xFFCCEFE3),
    secondary = JadeSecondaryDark,
    onSecondary = Color(0xFF0F1B17),
    tertiary = JadeTertiaryDark,
    onTertiary = Color(0xFF3F2E00),
    background = JadeBackgroundDark,
    onBackground = JadeTextLight,
    surface = JadeSurfaceDark,
    onSurface = JadeTextLight,
    surfaceVariant = JadeSurfaceVariantDark,
    onSurfaceVariant = JadeTextLight.copy(alpha = 0.8f),
    outline = JadeSecondaryDark.copy(alpha = 0.4f)
)

private val LightColorScheme = lightColorScheme(
    primary = JadePrimary,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFDCEFE7),
    onPrimaryContainer = Color(0xFF092217),
    secondary = JadeSecondary,
    onSecondary = Color.White,
    tertiary = JadeTertiary,
    onTertiary = Color.White,
    background = JadeBackgroundLight,
    onBackground = JadeTextDark,
    surface = JadeSurfaceLight,
    onSurface = JadeTextDark,
    surfaceVariant = JadeSurfaceVariantLight,
    onSurfaceVariant = JadeTextDark,
    outline = JadeBorderLight
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
