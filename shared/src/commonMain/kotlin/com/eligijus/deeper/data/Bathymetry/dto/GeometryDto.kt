package com.eligijus.deeper.data.Bathymetry.dto

import kotlinx.serialization.Serializable

@Serializable
data class GeometryDto(
    val type: String,
    val coordinates: List<List<List<Double>>>
)