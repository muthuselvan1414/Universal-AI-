package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme =
  darkColorScheme(
    primary = PrimaryIndigo,
    secondary = HighlightCyan,
    tertiary = TechPurple,
    background = DeepSlateBackground,
    surface = SolidCardSurface,
    onPrimary = LightSlateText,
    onSecondary = DeepSlateBackground,
    onTertiary = LightSlateText,
    onBackground = LightSlateText,
    onSurface = LightSlateText
  )

private val LightColorScheme = DarkColorScheme // Standardize on futuristic dark scheme

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = true, // Default to dark theme for sci-fi atmosphere
  dynamicColor: Boolean = false, // Force consistent sci-fi brand colors
  content: @Composable () -> Unit,
) {
  val colorScheme = DarkColorScheme

  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}
