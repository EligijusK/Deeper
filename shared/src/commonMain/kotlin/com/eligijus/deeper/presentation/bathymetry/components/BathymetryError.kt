package com.eligijus.deeper.presentation.bathymetry.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.eligijus.deeper.domain.model.Scan
import com.eligijus.deeper.presentation.bathymetry.BathymetryScreen
import com.eligijus.deeper.presentation.bathymetry.BathymetryUiState

@Composable
fun BathymetryError(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = message,
            textAlign = TextAlign.Center
        )

        Button(
            onClick = onRetry
        ) {
            Text("Retry")
        }
    }
}

@Preview
@Composable
private fun BathymetryErrorPreview() {
    MaterialTheme {
        val scan = Scan(1, 55.277287, 21.328197, "", null, 1, 0)
        BathymetryError("Error", {  })
    }
}