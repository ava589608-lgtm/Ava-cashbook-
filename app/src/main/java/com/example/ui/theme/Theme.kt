package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = AvaBrass,
    onPrimary = Color.Black,
    primaryContainer = AvaTealDark,
    onPrimaryContainer = AvaBrassLight,
    secondary = AvaTealMedium,
    onSecondary = Color.White,
    secondaryContainer = AvaCardSecondaryDark,
    onSecondaryContainer = AvaInkDark,
    tertiary = AvaMoneyIn,
    background = AvaPaperDark,
    surface = AvaCardDark,
    onBackground = AvaInkDark,
    onSurface = AvaInkDark,
    surfaceVariant = AvaCardSecondaryDark,
    onSurfaceVariant = AvaMutedDark,
    outline = AvaLineDark
)

private val LightColorScheme = lightColorScheme(
    primary = AvaTeal,
    onPrimary = Color.White,
    primaryContainer = AvaBrassLight,
    onPrimaryContainer = AvaBrassDark,
    secondary = AvaBrass,
    onSecondary = Color.Black,
    secondaryContainer = AvaPaperLight,
    onSecondaryContainer = AvaTealDark,
    tertiary = AvaMoneyIn,
    background = AvaPaperLight,
    surface = AvaCardLight,
    onBackground = AvaInkLight,
    onSurface = AvaInkLight,
    surfaceVariant = AvaCardSecondaryLight,
    onSurfaceVariant = AvaMutedLight,
    outline = AvaLineLight
)

@Composable
fun AvaCashTheme(
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
