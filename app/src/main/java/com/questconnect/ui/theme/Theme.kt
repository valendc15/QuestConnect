package com.questconnect.ui.theme

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


import androidx.compose.material3.*

val LightColorScheme = lightColorScheme(
    primary = LightBlue,
    onPrimary = DarkBlue,
    primaryContainer = VeryLightBlue,
    onPrimaryContainer = DarkNavyBlue,
    secondary = LightGrayBlue,
    onSecondary = DarkBlue,
    tertiary = Red,
    background = AlmostWhiteBlue,
    onBackground = MediumBlue,
    surface = PureWhite,
    onSurface = DarkNavyBlue,
    error = Red,
    onError = PureWhiteOnError
)

val DarkColorScheme = darkColorScheme(
    primary = DeepBlue,
    onPrimary = LightGrayishBlue,
    primaryContainer = DeepNavyBlue,
    onPrimaryContainer = LightBlue,
    secondary = MutedGrayBlue,
    onSecondary = LightGrayishBlue,
    tertiary = Red,
    background = VeryDarkBlue,
    onBackground = AlmostWhiteBlue,
    surface = NavySurfaceBlue,
    onSurface = AlmostWhiteBlue,
    error = Red,
    onError = LightBlueOnError
)

@Composable
fun QuestConnectTheme(
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