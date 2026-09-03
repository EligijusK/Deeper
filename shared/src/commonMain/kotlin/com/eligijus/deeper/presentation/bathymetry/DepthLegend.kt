package com.eligijus.deeper.presentation.bathymetry

import androidx.compose.ui.graphics.Color

data class DepthRange(
    val label: String,
    val minDepth: Double,
    val maxDepth: Double?,
    val color: Color
)

val depthRanges = listOf(
    DepthRange("0–1 m", 0.0, 1.0, Color(0xFF79A0B3)),
    DepthRange("1–2 m", 1.0, 2.0, Color(0xFF4AB3E2)),
    DepthRange("2–3 m",2.0, 3.0, Color(0xFF28AAE5)),
    DepthRange("3–4 m",3.0, 4.0, Color(0xFF038CCE)),
    DepthRange("4–5 m", 4.0, 5.0, Color(0xFF037ABB)),
    DepthRange("5–6 m", 5.0, 6.0, Color(0xFF026CAC)),
    DepthRange("6–8 m", 6.0, 8.0, Color(0xFF1257A5)),
    DepthRange("8–10 m",8.0, 10.0, Color(0xFF0B3B86)),
    DepthRange("10–12 m",  10.0, 12.0, Color(0xFF222C79)),
    DepthRange("12+ m", 12.0, null, Color(0xFF151C65))
)

//val depthRanges = listOf(
//    DepthRange(
//        label = "0–2 m",
//        minDepth = 0.0,
//        maxDepth = 2.0,
//        color = Color.Cyan
//    ),
//    DepthRange(
//        label = "2–5 m",
//        minDepth = 2.0,
//        maxDepth = 5.0,
//        color = Color(0xFF46AAFF)
//    ),
//    DepthRange(
//        label = "5–10 m",
//        minDepth = 5.0,
//        maxDepth = 10.0,
//        color = Color(0xFF286EDC)
//    ),
//    DepthRange(
//        label = "10+ m",
//        minDepth = 10.0,
//        maxDepth = null,
//        color = Color(0xFF143C96)
//    )
//)

//fun depthColor(depth: Double): Color {
//    val range = depthRanges.firstOrNull { range ->
//        depth >= range.minDepth &&
//                (range.maxDepth == null || depth < range.maxDepth)
//    }
//
//    return range?.color?.copy(alpha = 0.4f)
//        ?: Color.Transparent
//}

//fun depthColor(depth: Double): Color {
//    return depthRanges
//        .firstOrNull { range -> // need to fix this place
//            depth >= range.minDepth &&
//                    (range.maxDepth?.let { depth < it } ?: true)
//        }
//        ?.color
//        ?.copy(alpha = 0.55f)
//        ?: Color.Gray.copy(alpha = 0.55f)
//}

fun depthColor(depth: Double): Color {
    val color = when {
        depth < 1.0 -> Color(0xFFB3E5FC)   // light cyan
        depth < 2.0 -> Color(0xFF4FC3F7)   // cyan-blue
        depth < 3.0 -> Color(0xFF29B6F6)   // light blue
        depth < 4.0 -> Color(0xFF039BE5)   // blue
        depth < 5.0 -> Color(0xFF0288D1)   // darker blue
        depth < 6.0 -> Color(0xFF0277BD)
        depth < 8.0 -> Color(0xFF0D47A1)
        depth < 10.0 -> Color(0xFF283593)  // indigo
        depth < 12.0 -> Color(0xFF1A237E)
        else -> Color(0xFF161D68)          // deepest
    }

    return color.copy(alpha = 0.7f)
}

fun depthStrokeColor(depth: Double): Color {
    return depthRanges.firstOrNull { range -> // this also
        depth >= range.minDepth &&
                (range.maxDepth == null || depth < range.maxDepth)
    }?.color ?: Color.Transparent
}