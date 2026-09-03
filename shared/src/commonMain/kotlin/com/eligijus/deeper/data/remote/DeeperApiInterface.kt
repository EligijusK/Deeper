package com.eligijus.deeper.data.remote

import com.eligijus.deeper.data.remote.dto.Bathymetry.BathymetryResponseDto
import com.eligijus.deeper.data.remote.dto.login.LoginResponseDto

interface DeeperApiInterface {
    suspend fun login(
        email: String,
        password: String
    ): ApiResult<LoginResponseDto>

    suspend fun getBathymetry(
        scanId: Long,
        token: String
    ): ApiResult<BathymetryResponseDto>

}