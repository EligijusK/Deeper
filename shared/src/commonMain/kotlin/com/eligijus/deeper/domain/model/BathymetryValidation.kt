package com.eligijus.deeper.domain.model

fun Bathymetry.hasRenderableBathymetry(): Boolean {
    return features.any { feature ->
        feature.depth.isFinite() &&
                feature.geometry.coordinates.count { point ->
                    point.latitude.isFinite() &&
                            point.longitude.isFinite() &&
                            point.latitude in -90.0..90.0 &&
                            point.longitude in -180.0..180.0
                } >= 3
    }
}