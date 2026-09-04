package com.eligijus.deeper.data.mapper

import com.eligijus.deeper.domain.request.RequestError

fun RequestError.toMessage(): String {
    return when (this) {
        RequestError.InvalidCredentials ->
            "Incorrect email or password."

        RequestError.AccessForbidden ->
            "You don't have permission to access this resource."

        RequestError.NetworkError ->
            "Unable to connect. Check your internet connection."

        RequestError.ServerError ->
            "Server is currently unavailable. Try again later."

        RequestError.UnknownError ->
            "Something went wrong. Please try again."


    }
}