package com.eligijus.deeper.presentation.bathymetry

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eligijus.deeper.data.mapper.toMessage
import com.eligijus.deeper.domain.request.BathymetryRequestOutcome
import com.eligijus.deeper.domain.request.LoginRequestOutcome
import com.eligijus.deeper.domain.usecase.BathymetryUseCase
import com.eligijus.deeper.presentation.login.LoginEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BathymetryViewModel(
    private val bathymetryUseCase: BathymetryUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(BathymetryUiState())
    val uiState = _uiState.asStateFlow()

    fun loadBathymetry(
        scanId: Long,
        token: String
    ) {
        if (_uiState.value.isLoading) {
            return
        }

        viewModelScope.launch {

            _uiState.update {
                it.copy(
                    isLoading = true,
                    bathymetry = null,
                    errorMessage = null
                )
            }

            when (
                val result = bathymetryUseCase(
                    scanId = scanId,
                    token = token
                )
            ) {
                is BathymetryRequestOutcome.Success -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            bathymetry = result.result,
                            errorMessage = null
                        )
                    }
                }

                is BathymetryRequestOutcome.Failure -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            bathymetry = null,
                            errorMessage = result.error.toMessage()
                        )
                    }
                }
            }
        }
    }
}