package com.eligijus.deeper.domain.auth

import com.eligijus.deeper.domain.model.LoginResult

sealed interface LoginOutcome {

    data class Success(
        val result: LoginResult
    ) : LoginOutcome

    data class Failure(
        val error: LoginError
    ) : LoginOutcome
}