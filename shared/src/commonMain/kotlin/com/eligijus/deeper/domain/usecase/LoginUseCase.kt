package com.eligijus.deeper.domain.usecase

import com.eligijus.deeper.domain.request.LoginRequestOutcome
import com.eligijus.deeper.domain.repository.`interface`.AuthRepositoryInterface

class LoginUseCase (
    private val authRepository: AuthRepositoryInterface
) {
    suspend operator fun invoke(
        email: String,
        password: String
    ): LoginRequestOutcome {
        return authRepository.login(
            email = email.trim(),
            password = password
        )
    }
}