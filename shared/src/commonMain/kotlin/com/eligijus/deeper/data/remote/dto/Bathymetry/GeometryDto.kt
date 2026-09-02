package com.eligijus.deeper.data.remote.dto.Bathymetry

import kotlinx.serialization.Serializable

@Serializable
data class GeometryDto(
    val type: String,
    val bbox: List<Double>,
    val coordinates: List<List<List<Double>>>
)