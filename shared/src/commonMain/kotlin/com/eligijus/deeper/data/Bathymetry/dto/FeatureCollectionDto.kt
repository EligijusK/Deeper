package com.eligijus.deeper.data.Bathymetry.dto

import kotlinx.serialization.Serializable

@Serializable
data class FeatureCollectionDto(
    val type: String,
    val features: List<FeatureDto>
)