package com.eligijus.deeper.domain.model

data class BathymetryGeometry(
    val boundingBox: BoundingBox?,
    val coordinates: List<GeoPoint>
)