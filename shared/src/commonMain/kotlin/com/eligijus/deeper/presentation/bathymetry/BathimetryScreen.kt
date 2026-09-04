package com.eligijus.deeper.presentation.bathymetry

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
import com.eligijus.deeper.presentation.bathymetry.components.BathymetryError
import deeper.shared.generated.resources.Res
import deeper.shared.generated.resources.arrow_black_ios
import org.jetbrains.compose.resources.painterResource


@Composable
fun BathymetryScreen(
    scan: Scan,
    state: BathymetryUiState,
    onRetry: () -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        scan.name?.trim()
                        ?.takeIf { it.isNotEmpty() }
                        ?: "Unnamed scan"
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBackClick
                    ) {
                        Icon(
                            painter = painterResource(
                                Res.drawable.arrow_black_ios
                            ),
                            contentDescription = "Back"
                        )
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
            
            Box(
                modifier = Modifier
                    .fillMaxSize(),

            ) {


            }
            when {
                state.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                state.errorMessage != null -> {
                    BathymetryError(
                        message = state.errorMessage,
                        onRetry = onRetry,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                state.bathymetry != null -> {
                    BathymetryMap(
                        bathymetry = state.bathymetry,
                        modifier = Modifier.fillMaxSize()
                    )
                    if(state.bathymetry.features.isNotEmpty()) {
                        DepthLegendCard(
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .padding(16.dp)
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun BathymetryScreenPreview() {
    MaterialTheme {
        val scan = Scan(1, 55.277287, 21.328197, "", null, 1, 0)
        BathymetryScreen(
            scan = scan,
            state = BathymetryUiState(
                isLoading = false,
                bathymetry = null,
                errorMessage = "Unable to load bathymetry."
            ),
            onRetry = {},
            onBackClick = {}
        )
    }
}