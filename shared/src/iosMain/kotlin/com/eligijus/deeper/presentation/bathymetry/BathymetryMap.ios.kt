package com.eligijus.deeper.presentation.bathymetry

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.UIKitInteropInteractionMode
import androidx.compose.ui.viewinterop.UIKitInteropProperties
import com.eligijus.deeper.domain.model.Bathymetry
import androidx.compose.ui.viewinterop.UIKitView
import kotlinx.cinterop.ExperimentalForeignApi

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun BathymetryMap(
    bathymetry: Bathymetry,
    modifier: Modifier
) {
    val factory = LocalIosMapFactory.current

    val controller = remember(factory) {
        factory.createMapController()
    }

    val bounds = bathymetry.boundingBox

    val startLocation =
        bathymetry.scansGeoData.firstOrNull()?.let { scan ->
            scan.startLocation
                ?: scan.coordinates.firstOrNull()
        }

    val hasBathymetry = bathymetry.features.isNotEmpty()


    Box(
        modifier = modifier.fillMaxSize()
    ) {

        val properties = UIKitInteropProperties(
            interactionMode = UIKitInteropInteractionMode.Cooperative()
        )
        UIKitView(
            factory = {
                controller.view()
            },
            modifier = Modifier.fillMaxSize(),
            properties = properties
        )

        ZoomControls(
            onZoomIn = {
                controller.zoomIn()
            },
            onZoomOut = {
                controller.zoomOut()
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(
                    end = 16.dp,
                    bottom = 24.dp
                )
        )
    }

    LaunchedEffect(
        bathymetry,
        bounds,
        startLocation,
        hasBathymetry
    ) {
        controller.clear()

        bathymetry.features
            .sortedBy { it.depth }
            .forEach { feature ->

                val points = feature.geometry.coordinates

                if (points.size >= 3) {
                    controller.beginPolygon(
                        fillColorArgb = depthColor(
                            feature.depth
                        ).toArgb(),

                        strokeColorArgb = depthStrokeColor(
                            feature.depth
                        ).toArgb(),

                        strokeWidth = 1.0,
                        zIndex = feature.depth
                    )

                    points.forEach { point ->
                        controller.addPolygonPoint(
                            latitude = point.latitude,
                            longitude = point.longitude
                        )
                    }

                    controller.endPolygon()
                }
            }

        if (startLocation != null) {
            controller.setMarker(
                latitude = startLocation.latitude,
                longitude = startLocation.longitude,
                title = "Scan location"
            )
        }

        if (hasBathymetry && bounds != null) {
            controller.fitBounds(
                minLatitude = bounds.minLatitude,
                minLongitude = bounds.minLongitude,
                maxLatitude = bounds.maxLatitude,
                maxLongitude = bounds.maxLongitude,
                padding = 80.0
            )
        } else if (startLocation != null) {
            controller.moveCamera(
                latitude = startLocation.latitude,
                longitude = startLocation.longitude,
                zoom = 16.0
            )
        }
    }
}