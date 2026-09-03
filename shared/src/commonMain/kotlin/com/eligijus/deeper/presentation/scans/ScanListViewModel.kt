package com.eligijus.deeper.presentation.scans

import androidx.lifecycle.ViewModel
import com.eligijus.deeper.domain.model.Scan
import com.eligijus.deeper.domain.usecase.GetBathymetryAvailabilityUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ScanListViewModel(
    private val getBathymetryAvailabilityUseCase:
    GetBathymetryAvailabilityUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ScanListUiState())
    val uiState = _uiState.asStateFlow()

    fun refreshAvailability(
        scans: List<Scan>
    ) {
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