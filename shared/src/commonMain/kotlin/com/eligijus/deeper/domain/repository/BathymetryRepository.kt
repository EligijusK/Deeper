package com.eligijus.deeper.domain.repository

import com.eligijus.deeper.data.mapper.toDomain
import com.eligijus.deeper.data.remote.ApiResult
import com.eligijus.deeper.data.remote.DeeperApiInterface
import com.eligijus.deeper.domain.repository.`interface`.ScanRepositoryInterface
import com.eligijus.deeper.domain.request.BathymetryRequestOutcome
import com.eligijus.deeper.domain.request.BathymetryRequestOutcome.*
import com.eligijus.deeper.domain.request.RequestError

data class BathymetryRepository(
    private val deeperApi: DeeperApiInterface
): ScanRepositoryInterface {

    override suspend fun getBathymetry(
        scanId: Long,
        token: String
    ): BathymetryRequestOutcome {
        return when (
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
    }

}
