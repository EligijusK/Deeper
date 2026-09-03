package com.eligijus.deeper.domain.model

fun Scan.hasValidLocation(): Boolean {
    return id > 0 &&
            latitude.isFinite() &&
            longitude.isFinite() &&
            latitude in -90.0..90.0 &&
            longitude in -180.0..180.0
}