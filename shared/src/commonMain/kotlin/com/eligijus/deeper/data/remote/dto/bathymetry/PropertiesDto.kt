package com.eligijus.deeper.data.remote.dto.bathymetry

import kotlinx.serialization.Serializable

@Serializable
data class PropertiesDto(
    val depth: Double,
    val id: String
)