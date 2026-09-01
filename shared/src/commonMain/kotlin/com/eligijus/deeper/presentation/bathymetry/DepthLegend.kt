package com.eligijus.deeper.presentation.bathymetry

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.eligijus.deeper.presentation.login.LoginUiState

@Composable
fun DepthLegend(
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Text(
                text = "Depth",
                style = MaterialTheme.typography.titleSmall
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Text("0–1 m")
            Text("1–2 m")
            Text("2–3 m")
            Text("3–4 m")
        }
    }
}

@Preview
@Composable
private fun DepthLegendPreview() {
    MaterialTheme {
        DepthLegend(Modifier)
    }
}