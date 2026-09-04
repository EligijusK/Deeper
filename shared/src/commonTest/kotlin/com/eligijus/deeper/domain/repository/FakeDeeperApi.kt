package com.eligijus.deeper.domain.repository

import com.eligijus.deeper.data.remote.ApiResult
import com.eligijus.deeper.data.remote.DeeperApiInterface
import com.eligijus.deeper.data.remote.dto.bathymetry.BathymetryResponseDto
import com.eligijus.deeper.data.remote.dto.login.LoginResponseDto

class FakeDeeperApi : DeeperApiInterface {

    var bathymetryCallCount = 0

    var bathymetryResult: ApiResult<BathymetryResponseDto> =
        ApiResult.NetworkError
    var loginResult: ApiResult<LoginResponseDto> =
        ApiResult.UnknownError

    override suspend fun login(
        email: String,
        password: String
    ): ApiResult<LoginResponseDto> {
        return loginResult
    }

    override suspend fun getBathymetry(
        scanId: Long,
        token: String
    ): ApiResult<BathymetryResponseDto> {
        bathymetryCallCount++
        return bathymetryResult
    }
}