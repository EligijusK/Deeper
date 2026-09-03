package com.eligijus.deeper.domain.usecase

import com.eligijus.deeper.domain.model.BathymetryAvailability
import com.eligijus.deeper.domain.repository.`interface`.ScanRepositoryInterface

class GetBathymetryAvailabilityUseCase(
    private val scanRepository: ScanRepositoryInterface
) {
    operator fun invoke(scanId: Long): BathymetryAvailability {
        return scanRepository.getBathymetryAvailability(scanId)
    }
}