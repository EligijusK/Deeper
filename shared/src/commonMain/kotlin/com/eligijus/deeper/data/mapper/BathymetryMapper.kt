package com.eligijus.deeper.data.mapper

import com.eligijus.deeper.data.remote.dto.Bathymetry.BathymetryResponseDto
import com.eligijus.deeper.data.remote.dto.Bathymetry.FeatureDto
import com.eligijus.deeper.domain.model.Bathymetry
import com.eligijus.deeper.domain.model.BathymetryPolygon
import com.eligijus.deeper.domain.model.GeoPoint

fun BathymetryResponseDto.toDomain(): Bathymetry {
    return Bathymetry(
        polygons = bathymetry.features.mapNotNull { feature ->
            feature.toDomain()
        }
    )
}

private fun FeatureDto.toDomain(): BathymetryPolygon? {
    if (geometry.type != "Polygon") {
        return null
    }

    val outerRing = geometry.coordinates.firstOrNull()
        ?: return null

    val points = outerRing.mapNotNull { coordinate ->
        if (coordinate.size < 2) {
            null
        } else {
            GeoPoint(
                latitude = coordinate[1],
                longitude = coordinate[0]
            )
        }
    }

    if (points.size < 3) {
        return null
    }

    return BathymetryPolygon(
        depth = properties.depth,
        coordinates = points
    )
}