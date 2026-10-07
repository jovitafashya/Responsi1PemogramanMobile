package com.example.bmkggempa.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColors = lightColorScheme(primary = BluePrimary, secondary = BlueDark, tertiary = BlueLight)
private val DarkColors = darkColorScheme(primary = DarkBlue, secondary = BlueLight, surface = DarkSurface)

@Composable
fun BmkgGempaTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = if (isSystemInDarkTheme()) DarkColors else LightColors, typography = AppTypography, content = content)
}
