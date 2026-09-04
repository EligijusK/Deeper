package com.eligijus.deeper.data.remote.dto.bathymetry

import kotlinx.serialization.Serializable

@Serializable
data class FeatureCollectionDto(
    val type: String,
    val bbox: List<Double>,
    val features: List<FeatureDto>
)