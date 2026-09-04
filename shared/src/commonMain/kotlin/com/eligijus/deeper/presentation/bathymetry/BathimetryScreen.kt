package com.eligijus.deeper.presentation.bathymetry

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.eligijus.deeper.domain.model.Bathymetry
import com.eligijus.deeper.domain.model.BathymetryFeature
import com.eligijus.deeper.domain.model.BathymetryGeometry
import com.eligijus.deeper.domain.model.BoundingBox
import com.eligijus.deeper.domain.model.GeoPoint
import com.eligijus.deeper.domain.model.Scan
import com.eligijus.deeper.domain.model.ScanGeoData
import com.eligijus.deeper.presentation.bathymetry.components.BathymetryError
import com.eligijus.deeper.presentation.login.LoginScreen
import com.eligijus.deeper.presentation.login.LoginUiState
import deeper.shared.generated.resources.Res
import deeper.shared.generated.resources.compose_multiplatform
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun BathymetryScreen(
    scan: Scan,
    token: String,
    onBackClick: () -> Unit,
    viewModel: BathymetryViewModel = koinViewModel(),
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(scan.id) {
        viewModel.loadBathymetry(
            scanId = scan.id,
            token = token
        )
    }
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(scan.name.toString())
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBackClick
                    ) {
//                        Icon(
//                            imageVector = painterResource(Res.drawable.compose_multiplatform.),
//                            contentDescription = "Back"
//                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {

            // Temporary map placeholder
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {

                when {
                    uiState.isLoading -> {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }

                    uiState.errorMessage != null -> {
                        BathymetryError(
                            message = uiState.errorMessage!!,
                            onRetry = {
                                viewModel.loadBathymetry(
                                    scanId = scan.id,
                                    token = token
                                )
                            },
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }

                    uiState.bathymetry != null -> {
                        BathymetryMap(
                            bathymetry = uiState.bathymetry!!,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }

            DepthLegendCard(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp)
            )
        }
    }
}

@Preview
@Composable
private fun BathymetryScreenPreview() {
    MaterialTheme {
        val scan = Scan(1, 55.277287, 21.328197, "", null, 1, 0)
        BathymetryScreen(scan, "", {})
    }
}