package com.eligijus.deeper.domain.request

sealed interface RequestError {
    data object InvalidCredentials : RequestError
    data object AccessForbidden : RequestError
    data object ServerError : RequestError
    data object NetworkError : RequestError
    data object UnknownError : RequestError
}

