package com.eligijus.deeper.presentation.login

import com.eligijus.deeper.domain.model.LoginResult

sealed interface LoginEvent {
    data class Success(
        val result: LoginResult
    ) : LoginEvent
}