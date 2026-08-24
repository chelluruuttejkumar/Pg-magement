package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = ImmersiveAccent,
    onPrimary = ImmersiveDeepPurple,
    primaryContainer = ImmersiveDeepPurple,
    onPrimaryContainer = ImmersiveLightPurple,
    secondary = ImmersiveAccent,
    onSecondary = ImmersiveBg,
    secondaryContainer = ImmersiveSurfaceVariant,
    onSecondaryContainer = ImmersiveLightPurple,
    tertiary = ImmersiveSuccess,
    onTertiary = ImmersiveBg,
    background = ImmersiveBg,
    onBackground = ImmersiveTextPrimary,
    surface = ImmersiveCard,
    onSurface = ImmersiveTextPrimary,
    surfaceVariant = ImmersiveSurfaceVariant,
    onSurfaceVariant = ImmersiveTextSecondary,
    outline = ImmersiveBorder,
    error = ImmersiveSosBg,
    onError = ImmersiveSosText
)

private val LightColorScheme = DarkColorScheme // Consistently utilize the signature Immersive UI aesthetic


@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Keep consistent branding colors
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
