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

private val LightColorScheme = lightColorScheme(
    primary = Primary,
    onPrimary = Color.White,
    primaryContainer = PrimaryContainerLight,
    onPrimaryContainer = Primary,
    secondary = Color(0xFF666666),
    onSecondary = OnBackgroundLight,
    background = BackgroundLight,
    onBackground = OnBackgroundLight,
    surface = SurfaceLight,
    onSurface = OnSurfaceLight,
    surfaceVariant = SurfaceElevatedLight,
    onSurfaceVariant = OnSurfaceVariantLight,
    outline = SurfaceBorderLight,
    error = Error,
    onError = Color.White
)

@Composable
fun JoSunaInventoryTheme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = JoSunaTypography,
        content = content
    )
}