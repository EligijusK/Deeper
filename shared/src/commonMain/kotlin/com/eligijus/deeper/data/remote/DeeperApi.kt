package com.eligijus.deeper.data.remote

import com.eligijus.deeper.data.remote.dto.Bathymetry.BathymetryResponseDto
import com.eligijus.deeper.data.remote.dto.login.LoginRequestDto
import com.eligijus.deeper.data.remote.dto.login.LoginResponseDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import kotlin.coroutines.cancellation.CancellationException

class DeeperApi (
    private val client: HttpClient
) {
    suspend fun login(
        email: String,
        password: String
    ): ApiResult<LoginResponseDto> {
        return try {
            val response = client.post(
                "https://bathus.staging.deeper.eu/api/login"
            ) {
                contentType(ContentType.Application.Json)

                setBody(
                    LoginRequestDto(
                        email = email,
                        password = password
                    )
                )
            }

            when {
                response.status.isSuccess() -> {
                    ApiResult.Success(
                        response.body<LoginResponseDto>()
                    )
                }

                response.status == HttpStatusCode.Unauthorized -> {
                    ApiResult.Unauthorized
                }

                response.status == HttpStatusCode.Forbidden -> {
                    ApiResult.Forbidden
                }

                response.status.value in 500..599 -> {
                    ApiResult.ServerError
                }

                else -> {
                    ApiResult.UnknownError
                }
            }


        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            ApiResult.NetworkError
        }
    }

    suspend fun getBathymetry(
        scanId: Long,
        token: String
    ): ApiResult<BathymetryResponseDto> {
        return try {
            val response = client.get(
                "https://bathus.staging.deeper.eu/api/geoData"
            ) {
                parameter("grid", "FAST")
                parameter("generator", "BS")
                parameter("scanIds", scanId)
                parameter("token", token)
            }

            when {
                response.status.isSuccess() -> {
                    ApiResult.Success(
                        response.body()
                    )
                }

                response.status == HttpStatusCode.Unauthorized -> {
                    ApiResult.Unauthorized
                }

                response.status == HttpStatusCode.Forbidden -> {
                    ApiResult.Forbidden
                }

                response.status.value in 500..599 -> {
                    ApiResult.ServerError
                }

                else -> {
                    ApiResult.UnknownError
                }
            }

        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            ApiResult.NetworkError
        }
    }

}

