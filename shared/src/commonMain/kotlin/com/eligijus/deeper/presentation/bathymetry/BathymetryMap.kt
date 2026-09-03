package com.eligijus.deeper.presentation.bathymetry

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

import com.eligijus.deeper.domain.model.Bathymetry

@Composable
expect fun BathymetryMap(
    bathymetry: Bathymetry,
    modifier: Modifier = Modifier
)

fun depthColor(depth: Double): Color {
    return when {
        depth < 2.0 -> Color(0x8064DCFF)

        depth < 5.0 -> Color(0x8046AAFF)

        depth < 10.0 -> Color(0x80286EDC)

        else -> Color(0x80143C96)
    }
}

fun depthStrokeColor(depth: Double): Color {
    return when {
        depth < 2.0 -> Color.Cyan
        depth < 5.0 -> Color.Blue
        depth < 10.0 -> Color(0xFF1E4F9A)
        else -> Color(0xFF0D2A5C)
    }
}