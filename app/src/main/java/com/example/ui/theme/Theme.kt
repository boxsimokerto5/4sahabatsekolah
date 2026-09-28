package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
  primary = PastelPeach,
  onPrimary = Color.White,
  primaryContainer = PastelPeachLight,
  onPrimaryContainer = PastelPeachDark,
  secondary = PastelLilac,
  onSecondary = Color.White,
  secondaryContainer = PastelLilacLight,
  onSecondaryContainer = PastelLilac,
  tertiary = PastelMint,
  onTertiary = Color.White,
  tertiaryContainer = PastelMintLight,
  onTertiaryContainer = PastelMint,
  background = SoftBackground,
  onBackground = SoftTextPrimary,
  surface = SoftSurface,
  onSurface = SoftTextPrimary,
  surfaceVariant = SoftSurfaceVariant,
  onSurfaceVariant = SoftTextSecondary
)

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  // Keep cheerful pastel palette consistent
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) {
  MaterialTheme(
    colorScheme = LightColorScheme,
    typography = Typography,
    content = content
  )
}
