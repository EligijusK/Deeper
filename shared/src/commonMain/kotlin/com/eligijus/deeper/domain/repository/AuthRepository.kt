package com.eligijus.deeper.domain.repository

import com.eligijus.deeper.data.mapper.toDomain
import com.eligijus.deeper.data.remote.ApiResult
import com.eligijus.deeper.data.remote.DeeperApiInterface
import com.eligijus.deeper.domain.request.RequestError
import com.eligijus.deeper.domain.request.LoginRequestOutcome
import com.eligijus.deeper.domain.request.LoginRequestOutcome.*
import com.eligijus.deeper.domain.model.LoginResult
import com.eligijus.deeper.domain.repository.`interface`.AuthRepositoryInterface

class AuthRepository(
    private val api: DeeperApiInterface
) : AuthRepositoryInterface {

    override suspend fun login(
        email: String,
        password: String
    ): LoginRequestOutcome {

        return when (
            val result = api.login(
                email = email,
                password = password
            )
        ) {
            is ApiResult.Success -> {
                val response = result.data

                Success(
                    LoginResult(
                        token = response.login.token,
                        userId = response.login.userId,
                        scans = response.scans.map {
                            it.toDomain()
                        }
                    )
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