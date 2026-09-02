package com.eligijus.deeper.data.remote.dto.Bathymetry

import kotlinx.serialization.Serializable

@Serializable
data class GeometryDto(
    val type: String,
    val coordinates: List<List<List<Double>>>
)