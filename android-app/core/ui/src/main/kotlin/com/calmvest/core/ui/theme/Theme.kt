package com.calmvest.core.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = CalmGreen,
    onPrimary = CalmOnPrimary,
    primaryContainer = CalmGreenPale,
    onPrimaryContainer = CalmGreen,
    secondary = CalmGreenLight,
    onSecondary = CalmOnPrimary,
    secondaryContainer = CalmGreenPale,
    onSecondaryContainer = CalmGreen,
    background = CalmBackground,
    onBackground = CalmOnBackground,
    surface = CalmSurface,
    onSurface = CalmOnSurface,
    onSurfaceVariant = CalmOnSurfaceVariant,
    error = CalmError
)

private val DarkColorScheme = darkColorScheme(
    primary = CalmGreenLight,
    onPrimary = CalmOnBackground,
    primaryContainer = CalmGreen,
    onPrimaryContainer = CalmGreenPale,
    secondary = CalmGreenLighter,
    onSecondary = CalmOnBackground,
    background = CalmOnBackground,
    onBackground = CalmBackground,
    surface = CalmOnSurface,
    onSurface = CalmBackground,
    error = CalmError
)

@Composable
fun CalmvestTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = CalmvestTypography,
        content = content
    )
}
