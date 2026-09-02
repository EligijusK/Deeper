package com.eligijus.deeper.data.remote.dto.login

import kotlinx.serialization.Serializable

@Serializable
data class LoginResponseDto(
    val login: LoginDto,
    val scans: List<ScanDto>
)

@Serializable
data class LoginDto(
    val token: String,
    val userId: Long,
    val validated: Boolean
)

@Serializable
data class ScanDto(
    val id: Long,
    val lat: Double,
    val lon: Double,
    val name: String?,
    val date: String?,
    val scanPoints: Int,
    val mode: Int
)