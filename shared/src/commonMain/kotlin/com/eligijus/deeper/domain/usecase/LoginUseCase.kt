package com.eligijus.deeper.domain.usecase

import com.eligijus.deeper.domain.auth.LoginOutcome
import com.eligijus.deeper.domain.repository.`interface`.AuthRepositoryInterface

class LoginUseCase (
    private val authRepositoryInterface: AuthRepositoryInterface
) {
    suspend operator fun invoke(
        email: String,
        password: String
    ): LoginOutcome {
        return authRepositoryInterface.login(
            email = email.trim(),
            password = password
        )
    }
}