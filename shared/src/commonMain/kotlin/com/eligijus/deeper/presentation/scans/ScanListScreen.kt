package com.eligijus.deeper.presentation.scans

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ScanListScreen(
    scans: List<ScanUiModel>,
    onScanClick: (ScanUiModel) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(
            items = scans
        ) { scan ->
            ScanCard(
                scan = scan,
                onClick = {
                    onScanClick(scan)
                }
            )
        }
    }
}

@Preview
@Composable
private fun ScanListScreenPreview() {
    MaterialTheme {
        val scanList: List<ScanUiModel> = listOf<ScanUiModel> (
            ScanUiModel(1, "Test", "2025-05-10", "15:00", 5),
            ScanUiModel(2, "Test1", "2025-05-10", "14:00", 5),
            ScanUiModel(3, "Test2", "2025-05-10", "18:00", 5),
            ScanUiModel(4, "Test3", "2025-05-10", "13:00", 5),
            ScanUiModel(5, "Test4", "2025-05-10", "05:00", 5)
        )

        ScanListScreen(scanList, {}, Modifier)
    }
}