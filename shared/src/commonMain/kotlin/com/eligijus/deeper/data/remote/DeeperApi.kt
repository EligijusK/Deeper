package com.eligijus.deeper.data.remote

import com.eligijus.deeper.data.remote.dto.LoginRequestDto
import com.eligijus.deeper.data.remote.dto.LoginResponseDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
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