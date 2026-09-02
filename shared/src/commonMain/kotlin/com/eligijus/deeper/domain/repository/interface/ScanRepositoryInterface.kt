package com.eligijus.deeper.domain.repository.`interface`

interface ScanRepositoryInterface {
    suspend fun getBathymetry(
        scanId: Long,
        token: String
    ): BathymetryOutcomeInterface
}