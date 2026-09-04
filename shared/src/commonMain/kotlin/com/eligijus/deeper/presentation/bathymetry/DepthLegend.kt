package com.eligijus.deeper.presentation.bathymetry

import androidx.compose.ui.graphics.Color

data class DepthRange(
    val label: String,
    val minDepth: Double,
    val maxDepth: Double?,
    val color: Color
)

val depthStrokeColorRanges = listOf(
    DepthRange("0–1 m", 0.0, 1.0, Color(0xFF79A0B3)),
    DepthRange("1–2 m", 1.0, 2.0, Color(0xFF4AB3E2)),
    DepthRange("2–3 m",2.0, 3.0, Color(0xFF28AAE5)),
    DepthRange("3–4 m",3.0, 4.0, Color(0xFF038CCE)),
    DepthRange("4–5 m", 4.0, 5.0, Color(0xFF037ABB)),
    DepthRange("5–6 m", 5.0, 6.0, Color(0xFF026CAC)),
    DepthRange("6–8 m", 6.0, 8.0, Color(0xFF1257A5)),
    DepthRange("8–10 m",8.0, 10.0, Color(0xFF0B3B86)),
    DepthRange("10–12 m",  10.0, 12.0, Color(0xFF111752)),
    DepthRange("12+ m", 12.0, null, Color(0xFF0D1240))
)

val depthColorRanges = listOf(
    DepthRange("0–1 m", 0.0, 1.0, Color(0xFFB3E5FC)),
    DepthRange("1–2 m", 1.0, 2.0, Color(0xFF4FC3F7)),
    DepthRange("2–3 m",2.0, 3.0, Color(0xFF29B6F6)),
    DepthRange("3–4 m",3.0, 4.0, Color(0xFF039BE5)),
    DepthRange("4–5 m", 4.0, 5.0, Color(0xFF0288D1)),
    DepthRange("5–6 m", 5.0, 6.0, Color(0xFF0277BD)),
    DepthRange("6–8 m", 6.0, 8.0, Color(0xFF0D47A1)),
    DepthRange("8–10 m",8.0, 10.0, Color(0xFF283593)),
    DepthRange("10–12 m",  10.0, 12.0, Color(0xFF161D68)),
    DepthRange("12+ m", 12.0, null, Color(0xFF111650))
)


fun depthColor(depth: Double): Color {
    return depthColorRanges.first() { range -> // this also
        depth >= range.minDepth &&
                (range.maxDepth == null || depth < range.maxDepth)
    }.color.copy(alpha = 0.7f)

}

fun depthStrokeColor(depth: Double): Color {
    return depthStrokeColorRanges.firstOrNull { range -> // this also
        depth >= range.minDepth &&
                (range.maxDepth == null || depth < range.maxDepth)
    }?.color ?: Color.Transparent
}