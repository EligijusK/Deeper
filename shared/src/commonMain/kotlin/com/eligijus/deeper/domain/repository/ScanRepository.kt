package com.eligijus.deeper.domain.repository

import com.eligijus.deeper.data.mapper.toDomain
import com.eligijus.deeper.data.remote.ApiResult
import com.eligijus.deeper.data.remote.DeeperApiInterface
import com.eligijus.deeper.data.remote.dto.Bathymetry.BathymetryResponseDto
import com.eligijus.deeper.domain.model.BathymetryAvailability
import com.eligijus.deeper.domain.repository.`interface`.ScanRepositoryInterface
import com.eligijus.deeper.domain.request.BathymetryRequestOutcome
import com.eligijus.deeper.domain.request.BathymetryRequestOutcome.*
import com.eligijus.deeper.domain.request.RequestError

class ScanRepository(
    private val deeperApi: DeeperApiInterface
): ScanRepositoryInterface {

    private val bathymetryCache = mutableMapOf<Long, BathymetryRequestOutcome>()

    override suspend fun getBathymetry(
        scanId: Long,
        token: String
    ): BathymetryRequestOutcome {

        bathymetryCache[scanId]?.let {
            return it
        }

        val outcome = when (
            val result = deeperApi.getBathymetry(
                scanId = scanId,
                token = token
            )
        ) {
            is ApiResult.Success -> {
                Success(
                    result = result.data.toDomain()
                )
            }

            ApiResult.Unauthorized -> {
                Failure(
                    RequestError.InvalidCredentials
                )
            }

            ApiResult.Forbidden -> {
                Failure(
                    RequestError.AccessForbidden
                )
            }

            ApiResult.NetworkError -> {
                Failure(
                    RequestError.NetworkError
                )
            }

            ApiResult.ServerError -> {
                Failure(
                    RequestError.ServerError
                )
            }

            ApiResult.UnknownError -> {
                Failure(
                    RequestError.UnknownError
                )
            }
        }

        if (outcome is Success) {
            bathymetryCache[scanId] = outcome
        }

        return outcome



    }

    override fun getBathymetryAvailability(
        scanId: Long
    ): BathymetryAvailability {
        val cached = bathymetryCache[scanId]
            ?: return BathymetryAvailability.UNKNOWN

        return when (cached) {
            is BathymetryRequestOutcome.Success -> {
                if (cached.result.features.isNotEmpty()) {
                    BathymetryAvailability.AVAILABLE
                } else {
                    BathymetryAvailability.NOT_AVAILABLE
                }
            }

            is BathymetryRequestOutcome.Failure -> {
                BathymetryAvailability.UNKNOWN
            }
        }
    }

    fun clearCache() {
        bathymetryCache.clear()
    }

}
