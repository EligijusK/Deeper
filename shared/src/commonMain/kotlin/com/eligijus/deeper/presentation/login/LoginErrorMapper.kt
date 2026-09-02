package com.eligijus.deeper.presentation.login

import com.eligijus.deeper.domain.request.RequestError

fun RequestError.toMessage(): String {
    return when (this) {
        RequestError.InvalidCredentials ->
            "Incorrect email or password"

        RequestError.AccessForbidden ->
            "Access Forbidden"

        RequestError.NetworkError ->
            "Unable to connect. Check your internet connection."

        RequestError.ServerError ->
            "Server is currently unavailable"

        RequestError.UnknownError ->
            "Something went wrong"


    }
}