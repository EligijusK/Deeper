//
//  IOSMapFactory.swift
//  iosApp
//
//  Created by Eligijus Kiudys on 07/09/2026.
//

import UIKit
import GoogleMaps
import Shared

final class IOSMapFactory: IosMapFactory {

    func createMapController() -> any IosMapController {
        IOSMapControllerImpl()
    }
}

final class IOSMapControllerImpl: IosMapController {

    private let mapView: GMSMapView

    private var currentPath: GMSMutablePath?
    private var currentFillColor: UIColor?
    private var currentStrokeColor: UIColor?
    private var currentStrokeWidth: CGFloat = 1
    private var currentZIndex: Int32 = 0

    init() {
        let options = GMSMapViewOptions()

        options.camera = GMSCameraPosition.camera(
            withLatitude: 54.8985,
            longitude: 23.9036,
            zoom: 12
        )

        mapView = GMSMapView(options: options)
        mapView.mapType = .normal
    }

    func view() -> UIView {
        mapView
    }

    func clear() {
        mapView.clear()
    }

    func setMarker(
        latitude: Double,
        longitude: Double,
        title: String
    ) {
        let marker = GMSMarker()

        marker.position = CLLocationCoordinate2D(
            latitude: latitude,
            longitude: longitude
        )

        marker.title = title
        marker.map = mapView
    }

    func beginPolygon(
        fillColorArgb: Int32,
        strokeColorArgb: Int32,
        strokeWidth: Double,
        zIndex: Double
    ) {
        currentPath = GMSMutablePath()

        currentFillColor = uiColor(
            from: fillColorArgb
        )

        currentStrokeColor = uiColor(
            from: strokeColorArgb
        )

        currentStrokeWidth = CGFloat(strokeWidth)

        currentZIndex = Int32(
            (zIndex * 100).rounded()
        )
    }

    func addPolygonPoint(
        latitude: Double,
        longitude: Double
    ) {
        currentPath?.add(
            CLLocationCoordinate2D(
                latitude: latitude,
                longitude: longitude
            )
        )
    }

    func endPolygon() {
        guard let path = currentPath else {
            return
        }

        let polygon = GMSPolygon(path: path)

        polygon.fillColor = currentFillColor
        polygon.strokeColor = currentStrokeColor
        polygon.strokeWidth = currentStrokeWidth
        polygon.zIndex = currentZIndex
        polygon.map = mapView

        currentPath = nil
        currentFillColor = nil
        currentStrokeColor = nil
    }

    func fitBounds(
        minLatitude: Double,
        minLongitude: Double,
        maxLatitude: Double,
        maxLongitude: Double,
        padding: Double
    ) {
        let southWest = CLLocationCoordinate2D(
            latitude: minLatitude,
            longitude: minLongitude
        )

        let northEast = CLLocationCoordinate2D(
            latitude: maxLatitude,
            longitude: maxLongitude
        )

        let bounds = GMSCoordinateBounds(
            coordinate: southWest,
            coordinate: northEast
        )

        let update = GMSCameraUpdate.fit(
            bounds,
            withPadding: CGFloat(padding)
        )

        DispatchQueue.main.async {
            self.mapView.animate(with: update)
        }
    }

    func moveCamera(
        latitude: Double,
        longitude: Double,
        zoom: Double
    ) {
        let camera = GMSCameraPosition.camera(
            withLatitude: latitude,
            longitude: longitude,
            zoom: Float(zoom)
        )

        DispatchQueue.main.async {
            self.mapView.animate(to: camera)
        }
    }
    
    func zoomIn() {
        let zoom = min(
            self.mapView.camera.zoom + 1,
            self.mapView.maxZoom
        )

        mapView.animate(toZoom: zoom)
    }
    
    func zoomOut() {
        let zoom = max(
            self.mapView.camera.zoom - 1,
            self.mapView.minZoom
        )

        self.mapView.animate(toZoom: zoom)
    }

    private func uiColor(
        from argb: Int32
    ) -> UIColor {
        let value = UInt32(bitPattern: argb)

        let alpha = CGFloat(
            (value >> 24) & 0xFF
        ) / 255.0

        let red = CGFloat(
            (value >> 16) & 0xFF
        ) / 255.0

        let green = CGFloat(
            (value >> 8) & 0xFF
        ) / 255.0

        let blue = CGFloat(
            value & 0xFF
        ) / 255.0

        return UIColor(
            red: red,
            green: green,
            blue: blue,
            alpha: alpha
        )
    }
}
