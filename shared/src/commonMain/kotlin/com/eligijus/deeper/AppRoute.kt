package com.eligijus.deeper

import com.eligijus.deeper.domain.model.LoginResult

sealed interface AppRoute {
    data object Login : AppRoute

    data class ScanList(
        val loginResult: LoginResult
    ) : AppRoute

    data class Bathymetry(
        val scanId: Long,
        val token: String
    ) : AppRoute
}