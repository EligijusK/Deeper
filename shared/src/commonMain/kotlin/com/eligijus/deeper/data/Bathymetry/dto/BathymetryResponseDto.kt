package com.eligijus.deeper.data.Bathymetry.dto

import kotlinx.serialization.Serializable

@Serializable
data class BathymetryResponseDto(
    val bathymetry: FeatureCollectionDto
)