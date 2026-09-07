package com.eligijus.deeper.data.local

import androidx.room3.Dao
import androidx.room3.Query
import androidx.room3.Upsert

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