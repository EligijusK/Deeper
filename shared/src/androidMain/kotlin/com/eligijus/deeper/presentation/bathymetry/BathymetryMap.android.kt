package com.eligijus.deeper.presentation.bathymetry

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.eligijus.deeper.domain.model.Bathymetry
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Polygon
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
actual fun BathymetryMap(
    bathymetry: Bathymetry,
    modifier: Modifier
) {
    val cameraPositionState = rememberCameraPositionState()
    var mapLoaded by remember { mutableStateOf(false) }
    val bounds = bathymetry.boundingBox

    LaunchedEffect(mapLoaded, bounds) {
        if (mapLoaded && bounds != null) {
            val latLngBounds = LatLngBounds(
                LatLng(
                    bounds.minLatitude,
                    bounds.minLongitude
                ),
                LatLng(
                    bounds.maxLatitude,
                    bounds.maxLongitude
                )
            )

            cameraPositionState.animate(
                CameraUpdateFactory.newLatLngBounds(
                    latLngBounds,
                    80
                )
            )
        }
    }

    GoogleMap(
        modifier = modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        onMapLoaded = {
            mapLoaded = true
        }
    ) {
        bathymetry.features.forEach { feature ->

            val points = feature.geometry.coordinates.map { point ->
                LatLng(
                    point.latitude,
                    point.longitude
                )
            }

//            if (points.size >= 3) {
//                Polygon(
//                    points = points,
//                    fillColor = depthColor(feature.depth),
//                    strokeColor = depthStrokeColor(feature.depth),
//                    strokeWidth = 1f
//                )
//            }
        }
    }
}