package com.eligijus.deeper.data.local

import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity(tableName = "bathymetry_cache")
data class BathymetryCacheEntity(
    @PrimaryKey
    val scanId: Long,
    val bathymetryJson: String,
    val cachedAt: Long,
    val hasBathymetry: Boolean
)