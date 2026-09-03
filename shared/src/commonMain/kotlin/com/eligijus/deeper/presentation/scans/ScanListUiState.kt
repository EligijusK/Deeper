package com.eligijus.deeper.presentation.scans

import com.eligijus.deeper.domain.model.BathymetryAvailability

data class ScanListUiState(
    val availability: Map<Long, BathymetryAvailability> = emptyMap()
)