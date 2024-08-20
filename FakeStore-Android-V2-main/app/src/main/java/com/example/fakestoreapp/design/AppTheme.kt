package com.example.fakestoreapp.design

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun AppTheme(
    content: @Composable () -> Unit
) {

    val darkColorScheme = ColorScheme(
        primary = Color(0xFFFFFFFF), // White
        secondary = Color(0xFF2D3540), // Dark Blue
        background = Color(0xFF0D0D0D), // Black
        surface = Color(0xFF737373), // Gray
        error = Color(0xFFD32F2F), // Red
        onPrimary = Color(0xFFFFFFFF), // White
        onSecondary = Color(0xFFFFFFFF), //White
        onBackground = Color(0xFFFFFFFF), // White
        onSurface = Color(0xFFFFFFFF), // White
        onError = Color(0xFF000000), // Black
        inversePrimary = Color(0xFFFFFFFF), // White
        inverseSurface = Color(0xFF000000), // Black
        onErrorContainer = Color(0xFFB00020), // Dark Red
        onPrimaryContainer = Color(0xFF000000), // Black
        onSecondaryContainer = Color(0xFF000000), // Black
        onSurfaceVariant = Color(0xFF000000), // Black
        onTertiary = Color(0xFFA4A5A6), // Gray
        onTertiaryContainer = Color(0xFFFFFFFF), // White
        outline = Color(0xFF8D8D8D), // Medium Gray
        outlineVariant = Color(0xFF1C2026), // Dark blue
        primaryContainer = Color(0xFF1C2026), // Dark blue
        scrim = Color(0x99000000), // Dark Red with alpha
        secondaryContainer = Color(0xFF465059), // Gray
        surfaceTint = Color(0xFFFFFFFF), //
        surfaceVariant = Color(0xFFFFFFFF), //
        tertiary = Color(0xFFEBC040), //
        tertiaryContainer = Color(0xFF63853D), //
        errorContainer = Color(0xFFB00020), // Dark Red
        inverseOnSurface = Color(0xFFFFFFFF),
    )
    val typography = Typography()


    MaterialTheme(
        colorScheme = darkColorScheme,
        typography = typography,
        shapes = Shapes(),
        content = content
    )
}