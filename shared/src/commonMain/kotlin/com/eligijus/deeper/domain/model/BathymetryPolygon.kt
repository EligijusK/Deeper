package com.eligijus.deeper.domain.model

data class BathymetryPolygon(
    val depth: Double,
    val coordinates: List<GeoPoint>
)
