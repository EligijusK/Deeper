package com.eligijus.deeper.domain.usecase

import com.eligijus.deeper.domain.model.LoginResult
import com.eligijus.deeper.domain.repository.AuthRepository

class LoginUseCase (
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(
        email: String,
        password: String
    ): Result<LoginResult> {
        return authRepository.login(
            email = email.trim(),
            password = password
        )
    }
}