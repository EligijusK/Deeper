package com.eligijus.deeper.domain.repository

import com.eligijus.deeper.data.mapper.toDomain
import com.eligijus.deeper.data.remote.ApiResult
import com.eligijus.deeper.data.remote.DeeperApi
import com.eligijus.deeper.domain.auth.LoginError
import com.eligijus.deeper.domain.auth.LoginOutcome
import com.eligijus.deeper.domain.auth.LoginOutcome.*
import com.eligijus.deeper.domain.model.LoginResult
import com.eligijus.deeper.domain.repository.`interface`.AuthRepositoryInterface

class AuthRepository(
    private val api: DeeperApi
) : AuthRepositoryInterface {

    override suspend fun login(
        email: String,
        password: String
    ): LoginOutcome {

        return when (
            val result = api.login(
                email = email,
                password = password
            )
        ) {
            is ApiResult.Success -> {
                val response = result.data

                LoginOutcome.Success(
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
                    LoginError.InvalidCredentials
                )
            }

            ApiResult.Forbidden -> {
                Failure(
                    LoginError.AccessForbidden
                )
            }

            ApiResult.NetworkError -> {
                Failure(
                    LoginError.NetworkError
                )
            }

            ApiResult.ServerError -> {
                Failure(
                    LoginError.ServerError
                )
            }

            ApiResult.UnknownError -> {
                Failure(
                    LoginError.UnknownError
                )
            }
        }
    }
}