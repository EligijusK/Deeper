package com.eligijus.deeper.presentation.scans

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.eligijus.deeper.domain.model.BathymetryAvailability
import com.eligijus.deeper.domain.model.Scan

@Composable
fun ScanCard(
    scan: Scan,
    availability: BathymetryAvailability,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        onClick = onClick,
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = scan.name.toString(),
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Text(
                text = "${scan.date}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(
                modifier = Modifier.height(4.dp)
            )

            Text(
                text = "${scan.scanPoints} scan points",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(
                modifier = Modifier.height(4.dp)
            )

            when (availability) {
                BathymetryAvailability.AVAILABLE -> {
                    Text("Bathymetry available")
                }

                BathymetryAvailability.NOT_AVAILABLE -> {
                    Text("No bathymetry data")
                }

                BathymetryAvailability.UNKNOWN -> {
                    // I would display nothing here
                }
            }
        }
    }
}

@Preview
@Composable
private fun ScanCardPreview() {
    MaterialTheme {
        val scanUiModel: Scan = Scan(1, 55.277287, 21.328197, "", null, 1, 0)
        ScanCard(scanUiModel, BathymetryAvailability.UNKNOWN, {}, Modifier)
        ScanCard(scanUiModel, BathymetryAvailability.AVAILABLE, {}, Modifier)
        ScanCard(scanUiModel, BathymetryAvailability.NOT_AVAILABLE, {}, Modifier)
    }
}