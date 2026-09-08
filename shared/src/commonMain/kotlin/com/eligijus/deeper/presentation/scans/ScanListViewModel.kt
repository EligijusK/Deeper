package com.eligijus.deeper.presentation.scans

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eligijus.deeper.domain.model.Scan
import com.eligijus.deeper.domain.usecase.GetBathymetryAvailabilityUseCase
import com.eligijus.deeper.domain.usecase.LoadCachedBathymetryStatusesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ScanListViewModel(
    private val getBathymetryAvailabilityUseCase:
    GetBathymetryAvailabilityUseCase,
    private val loadCachedBathymetryStatusesUseCase:
    LoadCachedBathymetryStatusesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ScanListUiState())
    val uiState = _uiState.asStateFlow()

    fun refreshAvailability(
        scans: List<Scan>
    ) {
        viewModelScope.launch {

            loadCachedBathymetryStatusesUseCase()

            val availability = scans.associate { scan ->
                scan.id to getBathymetryAvailabilityUseCase(scan.id)
            }

            _uiState.update {
                it.copy(
                    availability = availability
                )
            }
        }
    }
}