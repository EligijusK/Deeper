package com.eligijus.deeper.data.remote

sealed interface ApiResult<out T> {

    data class Success<T>(
        val data: T
    ) : ApiResult<T>

    data object Unauthorized : ApiResult<Nothing>

    data object Forbidden : ApiResult<Nothing>

    data object ServerError : ApiResult<Nothing>

    data object NetworkError : ApiResult<Nothing>

    data object UnknownError : ApiResult<Nothing>
}