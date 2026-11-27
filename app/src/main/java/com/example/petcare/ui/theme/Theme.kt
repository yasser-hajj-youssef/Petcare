package com.example.petcare.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.graphics.Color
private val LightColorScheme = lightColorScheme(
    primary = Coral,
    onPrimary = OnCoral,
    primaryContainer = Coral,
    onPrimaryContainer = OnCoral,

    secondary = Teal,
    onSecondary = Ink,
    secondaryContainer = Color(0xFFCCFBF1),
    onSecondaryContainer = Ink,

    tertiary = Sunny,
    onTertiary = Ink,
    tertiaryContainer = Color(0xFFFFE5A3),
    onTertiaryContainer = Ink,

    background = Cream,
    onBackground = Ink,

    surface = Cream,
    onSurface = Ink,
    surfaceVariant = Color(0xFFF5E9DD),
    onSurfaceVariant = Ink,

    surfaceContainerLowest = Color.White,
    surfaceContainerLow = Color(0xFFFAF0E6),
    surfaceContainer = Color(0xFFF5E9DD),
    surfaceContainerHigh = Color(0xFFF0E2D5),
    surfaceContainerHighest = Color(0xFFEADBCD),

    outline = Color(0xFF9C8E82),
    outlineVariant = Color(0xFFD8C9BC),

    error = AlertRed,
    onError = Color.White
)


private val DarkColorScheme = darkColorScheme(
    primary = CoralLight,
    onPrimary = OnCoralLight,
    primaryContainer = CoralLight,
    onPrimaryContainer = OnCoralLight,

    secondary = TealLight,
    onSecondary = Navy,
    secondaryContainer = Color(0xFF164E49),
    onSecondaryContainer = Mist,

    tertiary = SunnyLight,
    onTertiary = Navy,
    tertiaryContainer = Color(0xFF594517),
    onTertiaryContainer = Mist,

    background = Navy,
    onBackground = Mist,

    surface = NavySurface,
    onSurface = Mist,
    surfaceVariant = Color(0xFF293344),
    onSurfaceVariant = Mist,

    surfaceContainerLowest = Navy,
    surfaceContainerLow = NavySurface,
    surfaceContainer = Color(0xFF222C3D),
    surfaceContainerHigh = Color(0xFF293344),
    surfaceContainerHighest = Color(0xFF303B4D),

    outline = Color(0xFF8993A5),
    outlineVariant = Color(0xFF465164),

    error = AlertRedLight,
    onError = Navy
)

@Composable
fun PetCareTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
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