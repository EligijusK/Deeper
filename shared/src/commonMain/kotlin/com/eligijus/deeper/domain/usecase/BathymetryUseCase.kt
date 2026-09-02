package com.eligijus.deeper.domain.usecase

import com.eligijus.deeper.domain.repository.`interface`.ScanRepositoryInterface
import com.eligijus.deeper.domain.request.BathymetryRequestOutcome

class BathymetryUseCase(
    private val scanRepository: ScanRepositoryInterface
) {

    suspend operator fun invoke(
        scanId: Long,
        token: String
    ): BathymetryRequestOutcome {
        return scanRepository.getBathymetry(
            scanId = scanId,
            token = token
        )
    }
}