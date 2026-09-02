package com.eligijus.deeper.domain.repository.`interface`

import com.eligijus.deeper.domain.request.LoginRequestOutcome

interface AuthRepositoryInterface {

    suspend fun login(
        email: String,
        password: String
    ): LoginRequestOutcome
}