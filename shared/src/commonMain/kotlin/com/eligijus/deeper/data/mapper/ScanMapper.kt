package com.eligijus.deeper.data.mapper

import com.eligijus.deeper.data.remote.dto.login.ScanDto
import com.eligijus.deeper.domain.model.Scan

fun ScanDto.toDomain(): Scan {
    return Scan(
        id = id,
        latitude = lat,
        longitude = lon,
        name = name,
        date = date,
        scanPoints = scanPoints,
        mode = mode
    )
}