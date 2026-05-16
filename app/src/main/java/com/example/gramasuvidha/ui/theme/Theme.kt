package com.example.gramasuvidha.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val LightColors = lightColorScheme(
    primary = PrimaryBlue,
    secondary = SecondaryOrange,
    background = BackgroundLight
)

private val DarkColors = darkColorScheme(
    primary = PrimaryLight,
    secondary = SecondaryOrange
)

@Composable
fun GramaSuvidhaTheme(
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (!useDarkTheme) {
        LightColors
    } else {
        DarkColors
    }

    MaterialTheme(
        colorScheme = colors,
        content = content
    )
}
