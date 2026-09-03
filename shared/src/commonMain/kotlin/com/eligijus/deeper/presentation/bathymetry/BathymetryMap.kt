package com.eligijus.deeper.presentation.bathymetry

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

import com.eligijus.deeper.domain.model.Bathymetry

@Composable
expect fun BathymetryMap(
    bathymetry: Bathymetry,
    modifier: Modifier = Modifier
)
