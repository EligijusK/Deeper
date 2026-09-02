package com.eligijus.deeper.data.remote.dto.Bathymetry

import kotlinx.serialization.Serializable

@Serializable
data class PropertiesDto(
    val depth: Double,
    val id: String
)