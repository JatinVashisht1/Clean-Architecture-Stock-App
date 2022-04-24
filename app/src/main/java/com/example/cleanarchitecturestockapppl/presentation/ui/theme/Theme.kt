package com.example.cleanarchitecturestockapppl.presentation.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColors(
    primary = Purple200,
    onPrimary = Purple700,
    onBackground = Teal200,
    background = Color(12, 29, 18, 255)
)

@Composable
fun CleanArchitectureStockAppPLTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colors = DarkColorPalette,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}