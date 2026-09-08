package com.eligijus.deeper.data.local

import androidx.room3.Dao
import androidx.room3.Query
import androidx.room3.Upsert
import com.eligijus.deeper.domain.model.CachedBathymetryStatus

@Dao
interface BathymetryCacheDao {

    @Query(
        """
        SELECT *   
        FROM bathymetry_cache
        WHERE scanId = :scanId
        LIMIT 1
        """
    )
    suspend fun getByScanId(
        scanId: Long
    ): BathymetryCacheEntity?

    @Query(
        """
        SELECT scanId
        FROM bathymetry_cache
        """
    )
    suspend fun getCachedScanIds(): List<Long>

    @Query(
        """
    SELECT scanId, hasBathymetry
    FROM bathymetry_cache
    """
    )
    suspend fun getCachedStatuses(): List<CachedBathymetryStatus>

    @Upsert
    suspend fun upsert(
        entity: BathymetryCacheEntity
    )

    @Query(
        """
        DELETE FROM bathymetry_cache
        WHERE scanId = :scanId
        """
    )
    suspend fun delete(
        scanId: Long
    )

    @Query(
        """
        DELETE FROM bathymetry_cache
        WHERE cachedAt < :timestamp
        """
    )
    suspend fun deleteOlderThan(
        timestamp: Long
    )

    @Query("DELETE FROM bathymetry_cache")
    suspend fun clear()

}