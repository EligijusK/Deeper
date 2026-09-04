package com.eligijus.deeper.data.mapper

import com.eligijus.deeper.data.remote.dto.bathymetry.BathymetryResponseDto
import com.eligijus.deeper.data.remote.dto.bathymetry.FeatureDto
import com.eligijus.deeper.data.remote.dto.bathymetry.ScanGeoDataDto
import com.eligijus.deeper.domain.model.Bathymetry
import com.eligijus.deeper.domain.model.BathymetryFeature
import com.eligijus.deeper.domain.model.BathymetryGeometry
import com.eligijus.deeper.domain.model.BoundingBox
import com.eligijus.deeper.domain.model.GeoPoint
import com.eligijus.deeper.domain.model.ScanGeoData

fun BathymetryResponseDto.toDomain(): Bathymetry {
    return Bathymetry(
        boundingBox = bathymetry.bbox.toBoundingBox(),
        features = bathymetry.features.mapNotNull { feature ->
            feature.toDomain()
        },
        scansGeoData = scansGeoData.map {
            it.toDomain()
        }
    )
}

fun FeatureDto.toDomain(): BathymetryFeature? {
    if (geometry.type != "Polygon") {
        return null
    }

    val ring = geometry.coordinates.firstOrNull()
        ?: return null

    val points = ring.mapNotNull { coordinate ->
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

    return BathymetryFeature(
        id = properties.id,
        depth = properties.depth,
        geometry = BathymetryGeometry(
            boundingBox = geometry.bbox.toBoundingBox(),
            coordinates = points
        )
    )
}

private fun List<Double>.toGeoPoint(): GeoPoint? {

    if (size < 2) {
        return null
    }

    val longitude = this[0]
    val latitude = this[1]

    if (
        latitude !in -90.0..90.0 ||
        longitude !in -180.0..180.0
    ) {
        return null
    }

    return GeoPoint(
        latitude = latitude,
        longitude = longitude
    )
}

private fun List<Double>.toBoundingBox(): BoundingBox? {

    if (size < 4) {
        return null
    }

    return BoundingBox(
        minLatitude = this[0],
        minLongitude = this[1],
        maxLatitude = this[2],
        maxLongitude = this[3]
    )
}

fun ScanGeoDataDto.toDomain(): ScanGeoData {
    return ScanGeoData(
        id = id,
        mode = mode,

        coordinates = coordinates
            ?.mapNotNull { coordinate ->
                coordinate.toGeoPoint()
            }
            .orEmpty(),

        polygons = polygons
            ?.map { polygon ->
                polygon.mapNotNull { coordinate ->
                    coordinate.toGeoPoint()
                }
            }
            .orEmpty(),

        startLocation = startLocation?.toGeoPoint(),

        size = size
    )
}

