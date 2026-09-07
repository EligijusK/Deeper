package com.eligijus.deeper.presentation.bathymetry

import kotlinx.cinterop.ExperimentalForeignApi
import platform.UIKit.UIView

@OptIn(ExperimentalForeignApi::class)
interface IosMapController {

    fun view(): UIView

    fun clear()

    fun setMarker(
        latitude: Double,
        longitude: Double,
        title: String
    )

    fun zoomIn()
    fun zoomOut()

    fun beginPolygon(
        fillColorArgb: Int,
        strokeColorArgb: Int,
        strokeWidth: Double,
        zIndex: Double
    )

    fun addPolygonPoint(
        latitude: Double,
        longitude: Double
    )

    fun endPolygon()

    fun fitBounds(
        minLatitude: Double,
        minLongitude: Double,
        maxLatitude: Double,
        maxLongitude: Double,
        padding: Double
    )

    fun moveCamera(
        latitude: Double,
        longitude: Double,
        zoom: Double
    )
}

@OptIn(ExperimentalForeignApi::class)
interface IosMapFactory {
    fun createMapController(): IosMapController
}