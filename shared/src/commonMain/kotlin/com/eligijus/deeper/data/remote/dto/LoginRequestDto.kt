package com.eligijus.deeper.data.remote.dto

import kotlinx.serialization.Serializable
import io.ktor.serialization.serialize;
@Serializable
data class LoginRequestDto(
    val email: String,
    val password: String
)
