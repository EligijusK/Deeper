package com.eligijus.deeper.presentation.login

import com.eligijus.deeper.domain.repository.`interface`.AuthRepositoryInterface
import com.eligijus.deeper.domain.request.LoginRequestOutcome
import com.eligijus.deeper.domain.request.RequestError

class FakeAuthRepository(
    var outcome: LoginRequestOutcome =
        LoginRequestOutcome.Failure(RequestError.UnknownError)
) : AuthRepositoryInterface {

    override suspend fun login(
        email: String,
        password: String
    ): LoginRequestOutcome {
        return outcome
    }
}