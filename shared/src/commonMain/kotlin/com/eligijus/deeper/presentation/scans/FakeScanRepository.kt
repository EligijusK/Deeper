package com.eligijus.deeper.presentation.scans

import com.eligijus.deeper.domain.model.BathymetryAvailability
import com.eligijus.deeper.domain.repository.`interface`.ScanRepositoryInterface

class FakeScanRepository(
    private val availability: Map<Long, BathymetryAvailability> =
        emptyMap()
) : ScanRepositoryInterface {

    override suspend fun getBathymetry(
        scanId: Long,
        token: String
    ) = error("Not used in this test")

    override fun getBathymetryAvailability(
        scanId: Long
    ): BathymetryAvailability {
        return availability[scanId]
            ?: BathymetryAvailability.UNKNOWN
    }
}