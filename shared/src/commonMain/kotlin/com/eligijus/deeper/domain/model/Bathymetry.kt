package com.eligijus.deeper.domain.model

data class Bathymetry(
    val boundingBox: BoundingBox?,
    val features: List<BathymetryFeature>,
    val scansGeoData: List<ScanGeoData>
)
