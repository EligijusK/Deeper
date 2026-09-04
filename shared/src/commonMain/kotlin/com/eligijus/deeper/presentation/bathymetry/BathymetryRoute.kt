package com.eligijus.deeper.presentation.bathymetry

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.eligijus.deeper.domain.model.Scan
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun BathymetryRoute(
    scan: Scan,
    token: String,
    onBackClick: () -> Unit
) {
    val viewModel = koinViewModel<BathymetryViewModel>()

    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(
        scan.id,
        token
    ) {
        viewModel.loadBathymetry(
            scanId = scan.id,
            token = token
        )
    }

    BathymetryScreen(
        scan = scan,
        state = state,
        onRetry = {
            viewModel.loadBathymetry(
                scanId = scan.id,
                token = token
            )
        },
        onBackClick = onBackClick
    )
}