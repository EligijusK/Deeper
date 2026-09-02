package com.eligijus.deeper.domain.model

data class Scan(
    val id: Long,
    val latitude: Double,
    val longitude: Double,
    val name: String?,
    val date: String?,
    val scanPoints: Int,
    val mode: Int
)