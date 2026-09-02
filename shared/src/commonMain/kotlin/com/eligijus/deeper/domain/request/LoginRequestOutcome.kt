package com.eligijus.deeper.domain.request

import com.eligijus.deeper.domain.model.LoginResult

sealed interface LoginRequestOutcome {

    data class Success(
        val result: LoginResult
    ) : LoginRequestOutcome

    data class Failure(
        val error: RequestError
    ) : LoginRequestOutcome
}