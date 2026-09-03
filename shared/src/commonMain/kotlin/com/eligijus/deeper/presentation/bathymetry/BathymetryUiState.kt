package com.eligijus.deeper.presentation.bathymetry

import com.eligijus.deeper.domain.model.Bathymetry

data class BathymetryUiState(
    val isLoading: Boolean = false,
    val bathymetry: Bathymetry? = null,
    val errorMessage: String? = null
)
