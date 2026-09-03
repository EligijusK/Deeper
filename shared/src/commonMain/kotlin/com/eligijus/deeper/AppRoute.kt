package com.eligijus.deeper

import com.eligijus.deeper.domain.model.LoginResult
import com.eligijus.deeper.domain.model.Scan

sealed interface AppRoute {
    data object Login : AppRoute

    data class ScanList(
        val loginResult: LoginResult
    ) : AppRoute

    data class Bathymetry(
        val scan: Scan,
        val token: String
    ) : AppRoute
}