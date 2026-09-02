package com.eligijus.deeper.domain.model

data class LoginResult(
    val token: String,
    val userId: Long,
    val scans: List<Scan>
)