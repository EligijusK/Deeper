package com.eligijus.deeper.presentation.bathymetry

import com.eligijus.deeper.domain.model.BathymetryAvailability
import com.eligijus.deeper.domain.repository.`interface`.ScanRepositoryInterface
import com.eligijus.deeper.domain.request.BathymetryRequestOutcome

class FakeBathymetryRepository(
    var result: BathymetryRequestOutcome
) : ScanRepositoryInterface {

    override suspend fun getBathymetry(
        scanId: Long,
        token: String
    ): BathymetryRequestOutcome {
        return result
    }

    override fun getBathymetryAvailability(
        scanId: Long
    ): BathymetryAvailability {
        return BathymetryAvailability.UNKNOWN
    }
}
