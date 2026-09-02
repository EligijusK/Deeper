package com.eligijus.deeper.domain.repository
import com.eligijus.deeper.domain.model.LoginResult

interface AuthRepository {

    suspend fun login(
        email: String,
        password: String
    ): Result<LoginResult>
}