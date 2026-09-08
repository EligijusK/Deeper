package com.eligijus.deeper.domain.repository

import com.eligijus.deeper.data.local.BathymetryCacheDao
import com.eligijus.deeper.data.local.BathymetryCacheEntity
import com.eligijus.deeper.data.local.toBathymetryResponseDto
import com.eligijus.deeper.data.local.toCacheJson
import com.eligijus.deeper.data.mapper.toDomain
import com.eligijus.deeper.data.remote.ApiResult
import com.eligijus.deeper.data.remote.DeeperApiInterface
import com.eligijus.deeper.domain.model.BathymetryAvailability
import com.eligijus.deeper.domain.repository.`interface`.ScanRepositoryInterface
import com.eligijus.deeper.domain.request.BathymetryRequestOutcome
import com.eligijus.deeper.domain.request.BathymetryRequestOutcome.*
import com.eligijus.deeper.domain.request.RequestError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.time.Clock
import kotlin.time.Duration.Companion.hours

class ScanRepository(
    private val deeperApi: DeeperApiInterface,
    private val cacheDao: BathymetryCacheDao,
    private val clock: Clock = Clock.System
): ScanRepositoryInterface {

    private data class MemoryCacheEntry(
        val outcome: Success,
        val cachedAt: Long
    )

    private val cachedScanIds = mutableSetOf<Long>()

    private val cachedStatuses = mutableMapOf<Long, BathymetryAvailability>()

    private val memoryCache = mutableMapOf<Long, MemoryCacheEntry>()

    private val cacheTtl = 24.hours

    override suspend fun getBathymetry(
        scanId: Long,
        token: String
    ): BathymetryRequestOutcome {

        val now = clock.now().toEpochMilliseconds()

        var staleCache: Success? = null

        memoryCache[scanId]?.let { cached ->

            if (isCacheFresh(
                    cachedAt = cached.cachedAt,
                    now = now
                )
            ) {
                println("Bathymetry source: MEMORY")
                return cached.outcome
            }

            staleCache = cached.outcome
        }

        if (staleCache == null) {

            cacheDao.getByScanId(scanId)?.let { entity ->

                try {
                    val outcome = withContext(
                        Dispatchers.Default
                    ) {
                        Success(
                            result = entity
                                .bathymetryJson
                                .toBathymetryResponseDto()
                                .toDomain()
                        )
                    }

                    if (isCacheFresh(
                            cachedAt = entity.cachedAt,
                            now = now
                        )
                    ) {
                        println("Bathymetry source: ROOM")

                        memoryCache[scanId] =
                            MemoryCacheEntry(
                                outcome = outcome,
                                cachedAt = entity.cachedAt
                            )

                        return outcome
                    }

                    // Don't delete it yet.
                    // It may be useful if the API is unavailable.
                    staleCache = outcome

                } catch (_: Exception) {

                    // Corrupt/incompatible cache.
                    cacheDao.delete(scanId)
                }
            }
        }

        println("Bathymetry source: API")

        return when (
            val result = deeperApi.getBathymetry(
                scanId = scanId,
                token = token
            )
        ) {
            is ApiResult.Success -> {
                val cachedAt =
                    clock.now().toEpochMilliseconds()

                val (bathymetry, json) =
                    withContext(Dispatchers.Default) {
                        result.data.toDomain() to
                                result.data.toCacheJson()
                    }

                cacheDao.upsert(
                    BathymetryCacheEntity(
                        scanId = scanId,
                        bathymetryJson = json,
                        cachedAt = cachedAt, // we'll handle expiration next
                        hasBathymetry = bathymetry.features.isNotEmpty()
                    )
                )

                cachedStatuses[scanId] =
                    if (bathymetry.features.isNotEmpty()) {
                        BathymetryAvailability.AVAILABLE
                    } else {
                        BathymetryAvailability.NOT_AVAILABLE
                    }

                val outcome = Success(
                    result = bathymetry
                )

                memoryCache[scanId] =
                    MemoryCacheEntry(
                        outcome = outcome,
                        cachedAt = cachedAt
                    )

                println("Bathymetry saved to ROOM")

                outcome
            }

            ApiResult.Unauthorized -> {
                Failure(
                    RequestError.InvalidCredentials
                )
            }

            ApiResult.Forbidden -> {
                Failure(
                    RequestError.AccessForbidden
                )
            }

            ApiResult.NetworkError -> {
                Failure(
                    RequestError.NetworkError
                )
            }

            ApiResult.ServerError -> {
                Failure(
                    RequestError.ServerError
                )
            }

            ApiResult.UnknownError -> {
                Failure(
                    RequestError.UnknownError
                )
            }
        }

    }

    override fun getBathymetryAvailability(
        scanId: Long
    ): BathymetryAvailability {
        memoryCache[scanId]?.let { cached ->
            return if (
                cached.outcome.result.features.isNotEmpty()
            ) {
                BathymetryAvailability.AVAILABLE
            } else {
                BathymetryAvailability.NOT_AVAILABLE
            }
        }

        return cachedStatuses[scanId]
            ?: BathymetryAvailability.UNKNOWN
    }

    override suspend fun loadCachedBathymetryStatuses() {

        cachedStatuses.clear()

        cacheDao
            .getCachedStatuses()
            .forEach { cache ->

                cachedStatuses[cache.scanId] =
                    if (cache.hasBathymetry) {
                        BathymetryAvailability.AVAILABLE
                    } else {
                        BathymetryAvailability.NOT_AVAILABLE
                    }
            }
    }

    private fun isCacheFresh(
        cachedAt: Long,
        now: Long
    ): Boolean {
        return now - cachedAt <
                cacheTtl.inWholeMilliseconds
    }

    fun clearCache() {
        memoryCache.clear()
    }

    suspend fun loadCachedScanIds() {
        cachedScanIds.clear()
        cachedScanIds.addAll(
            cacheDao.getCachedScanIds()
        )
    }

}


//deeperangler@gmail.com
//Deeper10899