package com.eligijus.deeper.data.local

import com.eligijus.deeper.data.remote.dto.bathymetry.BathymetryResponseDto
import kotlinx.serialization.json.Json

private val cacheJson = Json {
    ignoreUnknownKeys = true
    encodeDefaults = true
}

fun BathymetryResponseDto.toCacheJson(): String {
    return cacheJson.encodeToString(this)
}

fun String.toBathymetryResponseDto(): BathymetryResponseDto {
    return cacheJson.decodeFromString(this)
}