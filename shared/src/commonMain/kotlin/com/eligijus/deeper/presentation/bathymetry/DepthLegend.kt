package com.eligijus.deeper.presentation.bathymetry

import androidx.compose.ui.graphics.Color

data class DepthRange(
    val label: String,
    val minDepth: Double,
    val maxDepth: Double?,
    val color: Color
)

val depthRanges = listOf(
    DepthRange(
        label = "0–2 m",
        minDepth = 0.0,
        maxDepth = 2.0,
        color = Color.Cyan
    ),
    DepthRange(
        label = "2–5 m",
        minDepth = 2.0,
        maxDepth = 5.0,
        color = Color(0xFF46AAFF)
    ),
    DepthRange(
        label = "5–10 m",
        minDepth = 5.0,
        maxDepth = 10.0,
        color = Color(0xFF286EDC)
    ),
    DepthRange(
        label = "10+ m",
        minDepth = 10.0,
        maxDepth = null,
        color = Color(0xFF143C96)
    )
)

fun depthColor(depth: Double): Color {
    val range = depthRanges.firstOrNull { range ->
        depth >= range.minDepth &&
                (range.maxDepth == null || depth < range.maxDepth)
    }

    return range?.color?.copy(alpha = 0.4f)
        ?: Color.Transparent
}

fun depthStrokeColor(depth: Double): Color {
    return depthRanges.firstOrNull { range ->
        depth >= range.minDepth &&
                (range.maxDepth == null || depth < range.maxDepth)
    }?.color ?: Color.Transparent
}