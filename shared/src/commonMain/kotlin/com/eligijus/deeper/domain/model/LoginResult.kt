package com.eligijus.deeper.domain.model

import com.eligijus.deeper.data.remote.dto.ScanDto

data class LoginResult(
    val token: String,
    val userId: Long,
    val scans: List<ScanDto>
)