package com.joaquin.josuna_inventory.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = Primary,
    onPrimary = Color(0xFF0D0D0F),
    primaryContainer = PrimaryContainer,
    onPrimaryContainer = Primary,
    secondary = Color(0xFF8A8A96),
    onSecondary = OnBackground,
    background = Background,
    onBackground = OnBackground,
    surface = Surface,
    onSurface = OnSurface,
    surfaceVariant = SurfaceElevated,
    onSurfaceVariant = OnSurfaceVariant,
    outline = SurfaceBorder,
    error = Error,
    onError = Color.White
)

@Composable
fun JoSunaInventoryTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = JoSunaTypography,
        content = content
    )
}