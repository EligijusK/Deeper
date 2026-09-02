package com.eligijus.deeper.domain.repository
import com.eligijus.deeper.domain.auth.LoginOutcome

interface AuthRepositoryInterface {

    suspend fun login(
        email: String,
        password: String
    ): LoginOutcome
}