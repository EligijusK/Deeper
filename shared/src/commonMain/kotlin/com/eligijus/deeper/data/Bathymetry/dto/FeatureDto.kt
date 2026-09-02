package com.eligijus.deeper.data.Bathymetry.dto

import kotlinx.serialization.Serializable

@Serializable
data class FeatureDto(
    val properties: PropertiesDto,
    val geometry: GeometryDto
)
