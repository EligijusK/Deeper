package com.eligijus.deeper.data.remote.dto.Bathymetry

import kotlinx.serialization.Serializable

@Serializable
data class FeatureDto(
    val properties: PropertiesDto,
    val geometry: GeometryDto
)
