package com.eligijus.deeper.domain.repository.`interface`

import com.eligijus.deeper.domain.model.BathymetryAvailability
import com.eligijus.deeper.domain.request.BathymetryRequestOutcome

interface ScanRepositoryInterface {
    suspend fun getBathymetry(
        scanId: Long,
        token: String
    ): BathymetryRequestOutcome

    fun getBathymetryAvailability(
        scanId: Long
    ): BathymetryAvailability
}