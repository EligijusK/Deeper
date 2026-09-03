package com.eligijus.deeper.presentation.bathymetry

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.eligijus.deeper.domain.model.Scan
import com.eligijus.deeper.presentation.login.LoginScreen
import com.eligijus.deeper.presentation.login.LoginUiState
import deeper.shared.generated.resources.Res
import deeper.shared.generated.resources.compose_multiplatform
import org.jetbrains.compose.resources.painterResource

@Composable
fun BathymetryScreen(
    scan: Scan,
    state: BathymetryUiState,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
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

            when {
                state.isLoading -> {
                    CircularProgressIndicator()
                    println("Is Loading")
                }

                state.errorMessage != null -> {
                    Text(
                        text = state.errorMessage
                    )
                }

                state.bathymetry != null -> {
                    Text("Bathymetry loaded")
                    println("data: ${state.bathymetry?.features?.size}")
                    Text(
                        text = "Features: ${state.bathymetry.features.size}"
                    )

                    Text(
                        text = "Geo data: ${state.bathymetry.scansGeoData.size}"
                    )
                }
            }


            // Temporary map placeholder
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        MaterialTheme.colorScheme.surfaceVariant
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text("Bathymetry map")
            }

            DepthLegend(
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
        BathymetryScreen(scan, BathymetryUiState(), {}, Modifier)
    }
}