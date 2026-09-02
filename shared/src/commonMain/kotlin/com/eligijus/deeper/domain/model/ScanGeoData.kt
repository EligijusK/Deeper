package com.eligijus.deeper.domain.model

data class ScanGeoData(
    val id: Long,
    val mode: Int,
    val coordinates: List<GeoPoint>,
    val polygons: List<List<GeoPoint>>,
    val startLocation: GeoPoint?,
    val size: Long?
)