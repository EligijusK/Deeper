package com.eligijus.deeper.data.remote.dto.bathymetry

import kotlinx.serialization.Serializable

@Serializable
data class ScanGeoDataDto(
    val id: Long,
    val mode: Int,
    val coordinates: List<List<Double>>? = null,
    val polygons: List<List<List<Double>>>? = null,
    val startLocation: List<Double>? = null,
    val size: Double? = null
)