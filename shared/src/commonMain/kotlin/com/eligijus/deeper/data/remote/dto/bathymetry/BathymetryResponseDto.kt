package com.eligijus.deeper.data.remote.dto.bathymetry

import kotlinx.serialization.Serializable

@Serializable
data class BathymetryResponseDto(
    val bathymetry: FeatureCollectionDto,
    val scansGeoData: List<ScanGeoDataDto> = emptyList()
)