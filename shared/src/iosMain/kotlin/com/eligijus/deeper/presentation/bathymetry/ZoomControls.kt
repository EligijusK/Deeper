package com.eligijus.deeper.presentation.bathymetry

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ZoomControls(
    onZoomIn: () -> Unit,
    onZoomOut: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.width(48.dp),
        shape = RoundedCornerShape(8.dp),
        shadowElevation = 4.dp
    ) {
        Column(
            modifier = Modifier.width(48.dp)
        ) {
            IconButton(
                onClick = onZoomIn,
                modifier = Modifier.size(48.dp)
            ) {
                Text(
                    text = "+",
                    fontSize = 24.sp
                )
            }

            HorizontalDivider()

            IconButton(
                onClick = onZoomOut,
                modifier = Modifier.size(48.dp)
            ) {
                Text(
                    text = "−",
                    fontSize = 24.sp
                )
            }
        }
    }
}