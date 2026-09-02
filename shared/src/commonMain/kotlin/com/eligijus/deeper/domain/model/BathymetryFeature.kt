package com.eligijus.deeper.domain.model

data class BathymetryFeature(
    val id: String,
    val depth: Double,
    val geometry: BathymetryGeometry
)