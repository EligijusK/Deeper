package com.eligijus.deeper.domain.usecase


import com.eligijus.deeper.domain.repository.`interface`.ScanRepositoryInterface

class LoadCachedBathymetryStatusesUseCase(
    private val repository: ScanRepositoryInterface
) {
    suspend operator fun invoke() {
        repository.loadCachedBathymetryStatuses()
    }
}