package com.eligijus.deeper.presentation.bathymetry

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.eligijus.deeper.domain.model.Bathymetry
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Polygon
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
actual fun BathymetryMap(
    bathymetry: Bathymetry,
    modifier: Modifier
) {
    val cameraPositionState = rememberCameraPositionState()

    GoogleMap(
        modifier = modifier,
        cameraPositionState = cameraPositionState
    ) {
        bathymetry.features.forEach { feature ->

            val points = feature.geometry.coordinates.map { point ->
                LatLng(
                    point.latitude,
                    point.longitude
                )
            }

            if (points.size >= 3) {
                Polygon(
                    points = points,
                    fillColor = depthColor(feature.depth),
                    strokeColor = depthStrokeColor(feature.depth),
                    strokeWidth = 1f
                )
            }
        }
    }
}