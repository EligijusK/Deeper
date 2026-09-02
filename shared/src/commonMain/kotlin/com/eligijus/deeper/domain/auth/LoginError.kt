package com.eligijus.deeper.domain.auth

import com.eligijus.deeper.domain.model.LoginResult

sealed interface LoginError {
    data object InvalidCredentials : LoginError
    data object AccessForbidden : LoginError
    data object ServerError : LoginError
    data object NetworkError : LoginError
    data object UnknownError : LoginError
}

