package com.eligijus.deeper.presentation.scans

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.eligijus.deeper.domain.model.Scan
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ScanListRoute(
    scans: List<Scan>,
    onScanClick: (Scan) -> Unit
) {
    val viewModel = koinViewModel<ScanListViewModel>()
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(scans) {
        viewModel.refreshAvailability(scans)
    }

    ScanListScreen(
        scans = scans,
        state = state,
        onScanClick = onScanClick
    )
}