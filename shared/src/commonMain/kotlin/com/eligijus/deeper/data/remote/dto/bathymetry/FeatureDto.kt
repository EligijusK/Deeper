package com.eligijus.deeper.data.remote.dto.bathymetry

import kotlinx.serialization.Serializable

@Serializable
data class FeatureDto(
    val type: String,
    val properties: PropertiesDto,
    val geometry: GeometryDto
)
