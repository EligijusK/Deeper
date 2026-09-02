package com.eligijus.deeper.data.remote.dto.Bathymetry

import kotlinx.serialization.Serializable

@Serializable
data class BathymetryResponseDto(
    val bathymetry: FeatureCollectionDto
)