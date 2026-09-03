package com.eligijus.deeper.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eligijus.deeper.data.mapper.toMessage
import com.eligijus.deeper.domain.request.LoginRequestOutcome
import com.eligijus.deeper.domain.usecase.LoginUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel (
     private val loginUseCase: LoginUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())

    val uiState: StateFlow<LoginUiState> =
        _uiState.asStateFlow()

    private val _events =
        MutableSharedFlow<LoginEvent>()

    val events: SharedFlow<LoginEvent> =
        _events.asSharedFlow()

    fun onEmailChanged(email: String) {
        _uiState.update {
            it.copy(
                email = email,
                errorMessage = null
            )
        }
    }

    fun onPasswordChanged(password: String) {
        _uiState.update {
            it.copy(
                password = password,
                errorMessage = null
            )
        }
    }

    fun login() {
        val state = _uiState.value
        if (!state.canLogin) {
            return
        }

        viewModelScope.launch {

            _uiState.update {
                it.copy(
                    isLoading = true,
                    errorMessage = null
                )
            }

            when (
                val result = loginUseCase(
                    email = state.email,
                    password = state.password
                )
            ) {
                is LoginRequestOutcome.Success -> {

                    _uiState.update {
                        it.copy(
                            isLoading = false
                        )
                    }

                    _events.emit(
                        LoginEvent.Success(
                            result.result
                        )
                    )
                }

                is LoginRequestOutcome.Failure -> {

                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = result.error.toMessage()
                        )
                    }
                }
            }
        }
    }
}