package com.eligijus.deeper.presentation.scans

data class ScanUiModel(
    val id: Long,
    val name: String,
    val date: String,
    val time: String,
    val scanPoints: Int
)