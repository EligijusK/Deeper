package com.eligijus.deeper.presentation.bathymetry

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.eligijus.deeper.domain.model.Bathymetry

@Composable
actual fun BathymetryMap(
    bathymetry: Bathymetry,
    modifier: Modifier
) {
    Text(
        text = "Map is currently available on Android only",
        modifier = modifier
    )
}