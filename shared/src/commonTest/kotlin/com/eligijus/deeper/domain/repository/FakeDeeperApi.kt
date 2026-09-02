package com.eligijus.deeper.domain.repository

import com.eligijus.deeper.data.remote.ApiResult
import com.eligijus.deeper.data.remote.DeeperApiInterface
import com.eligijus.deeper.data.remote.dto.Bathymetry.BathymetryResponseDto
import com.eligijus.deeper.data.remote.dto.login.LoginResponseDto

class FakeDeeperApi : DeeperApiInterface {

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
        return ApiResult.UnknownError
    }
}