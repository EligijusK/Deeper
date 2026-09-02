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
import com.eligijus.deeper.domain.model.Scan

@Composable
fun ScanListScreen(
    scans: List<Scan>,
    onScanClick: (Scan) -> Unit,
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
        val scanList: List<Scan> = listOf<Scan> (
            Scan(1, 55.277287, 21.328197, "", null, 1, 0),
            Scan(1, 55.277287, 21.328197, "", null, 1, 0),
            Scan(1, 55.277287, 21.328197, "", null, 1, 0),
            Scan(1, 55.277287, 21.328197, "", null, 1, 0),
            Scan(1, 55.277287, 21.328197, "", null, 1, 0)
        )

        ScanListScreen(scanList, {}, Modifier)
    }
}