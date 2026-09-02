package com.eligijus.deeper.data.remote.dto.Bathymetry

import kotlinx.serialization.Serializable

@Serializable
data class FeatureCollectionDto(
    val type: String,
    val features: List<FeatureDto>
)