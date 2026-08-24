package com.example.ui.theme

import androidx.compose.ui.graphics.Color

// ==========================================
// Immersive UI Design Tokens
// ==========================================
val ImmersiveBg = Color(0xFF1C1B1F)           // Base dark canvas
val ImmersiveCard = Color(0xFF2B2930)         // Container / Card surface
val ImmersiveSurfaceVariant = Color(0xFF313033) // Icon tiles, secondary surfaces
val ImmersiveBorder = Color(0xFF49454F)       // Card & component borders
val ImmersiveAccent = Color(0xFFD0BCFF)       // Lavender primary accent
val ImmersiveDeepPurple = Color(0xFF381E72)   // Deep violet container / pill
val ImmersiveLightPurple = Color(0xFFEADDFF)  // Highlight / Title text
val ImmersiveTextPrimary = Color(0xFFE6E1E5)  // High contrast body text
val ImmersiveTextSecondary = Color(0xFFCAC4D0) // Subtitle text
val ImmersiveTextMuted = Color(0xFF938F99)    // Caption / timestamp text
val ImmersiveSosBg = Color(0xFFB3261E)        // Danger / SOS red button
val ImmersiveSosText = Color(0xFFF2B8B5)      // SOS badge text / light red
val ImmersiveSuccess = Color(0xFF4ADE80)      // Active / online green

// Mapped Palette Aliases for seamless theme propagation
val Navy900 = ImmersiveBg
val Navy800 = ImmersiveCard
val Navy700 = ImmersiveSurfaceVariant
val Navy500 = ImmersiveBorder

val Indigo600 = ImmersiveDeepPurple
val Indigo500 = Color(0xFF6750A4)
val Indigo400 = ImmersiveAccent
val Indigo100 = ImmersiveLightPurple

val Cyan500 = Color(0xFFB69DF8)
val Cyan400 = ImmersiveAccent
val Cyan100 = ImmersiveLightPurple

val Emerald600 = Color(0xFF16A34A)
val Emerald500 = Color(0xFF22C55E)
val Emerald400 = ImmersiveSuccess
val Emerald100 = Color(0xFFDCFCE7)

val Amber500 = Color(0xFFF59E0B)
val Amber400 = Color(0xFFFBBF24)
val Amber100 = Color(0xFFFEF3C7)

val Rose600 = ImmersiveSosBg
val Rose500 = ImmersiveSosBg
val Rose400 = ImmersiveSosText
val Rose100 = Color(0xFFFFE4E6)

// Neutral & Backgrounds
val BgLight = Color(0xFFF8FAFC)
val SurfaceLight = Color(0xFFFFFFFF)
val SurfaceVariantLight = Color(0xFFF1F5F9)

val BgDark = ImmersiveBg
val SurfaceDark = ImmersiveCard
val SurfaceVariantDark = ImmersiveSurfaceVariant
val GlassDark = Color(0xDD2B2930)
val GlassBorderDark = ImmersiveBorder

