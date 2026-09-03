package com.eligijus.deeper.presentation.login

import com.eligijus.deeper.domain.request.RequestError

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null
) {
    val canLogin: Boolean
        get() = email.isNotBlank() &&
                password.isNotBlank() &&
                !isLoading
}