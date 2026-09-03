package com.eligijus.deeper.domain.repository

import com.eligijus.deeper.data.remote.ApiResult
import com.eligijus.deeper.data.remote.dto.Bathymetry.BathymetryResponseDto
import com.eligijus.deeper.data.remote.dto.Bathymetry.FeatureCollectionDto
import com.eligijus.deeper.data.remote.dto.Bathymetry.FeatureDto
import com.eligijus.deeper.data.remote.dto.Bathymetry.GeometryDto
import com.eligijus.deeper.data.remote.dto.Bathymetry.PropertiesDto
import com.eligijus.deeper.domain.model.BathymetryAvailability
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class ScanRepositoryTest {

    private fun validBathymetryDto(): BathymetryResponseDto {
        return BathymetryResponseDto(
            bathymetry = FeatureCollectionDto(
                type = "FeatureCollection",
                bbox = listOf(
                    54.6977,
                    25.3094,
                    54.6980,
                    25.3100
                ),
                features = listOf(
                    FeatureDto(
                        type = "Feature",
                        properties = PropertiesDto(
                            depth = 1.666666666665,
                            id = "test-feature-0"
                        ),
                        geometry = GeometryDto(
                            type = "Polygon",
                            bbox = listOf(
                                54.6977,
                                25.3094,
                                54.6980,
                                25.3100
                            ),
                            coordinates = listOf(
                                listOf(
                                    listOf(
                                        25.3095,
                                        54.6978,
                                        1.666666666665
                                    ),
                                    listOf(
                                        25.3098,
                                        54.6978,
                                        1.666666666665
                                    ),
                                    listOf(
                                        25.3098,
                                        54.6980,
                                        1.666666666665
                                    ),
                                    listOf(
                                        25.3095,
                                        54.6978,
                                        1.666666666665
                                    )
                                )
                            )
                        )
                    )
                )
            ),
            scansGeoData = emptyList()
        )
    }

    private fun bathymetryDto(): BathymetryResponseDto {
        return BathymetryResponseDto(
            bathymetry = FeatureCollectionDto(
                type = "FeatureCollection",
                bbox = emptyList(),
                features = emptyList()
            ),
            scansGeoData = emptyList()
        )
    }

    @Test
    fun `getBathymetry caches successful response`() = runTest {
        val api = FakeDeeperApi().apply {
            bathymetryResult = ApiResult.Success(
                bathymetryDto()
            )
        }

        val repository = ScanRepository(api)

        repository.getBathymetry(
            scanId = 2434152,
            token = "token"
        )

        repository.getBathymetry(
            scanId = 2434152,
            token = "token"
        )

        assertEquals(
            1,
            api.bathymetryCallCount
        )
    }

    @Test
    fun `availability is unknown before bathymetry is loaded`() {
        val repository = ScanRepository(
            deeperApi = FakeDeeperApi()
        )

        assertEquals(
            BathymetryAvailability.UNKNOWN,
            repository.getBathymetryAvailability(2434152)
        )
    }

    @Test
    fun `availability is not available when bathymetry has no features`() = runTest {
        val api = FakeDeeperApi().apply {
            bathymetryResult = ApiResult.Success(
                bathymetryDto() // features = emptyList()
            )
        }

        val repository = ScanRepository(api)

        repository.getBathymetry(
            scanId = 2434163,
            token = "token"
        )

        assertEquals(
            BathymetryAvailability.NOT_AVAILABLE,
            repository.getBathymetryAvailability(2434163)
        )
    }

    @Test
    fun `availability is available when bathymetry has features`() = runTest {
        val api = FakeDeeperApi().apply {
            bathymetryResult = ApiResult.Success(
                validBathymetryDto()
            )
        }

        val repository = ScanRepository(api)

        repository.getBathymetry(
            scanId = 2434158,
            token = "token"
        )

        assertEquals(
            BathymetryAvailability.AVAILABLE,
            repository.getBathymetryAvailability(2434158)
        )
    }

    @Test
    fun `failed bathymetry request is not cached`() = runTest {
        val api = FakeDeeperApi()
        val repository = ScanRepository(api)

        api.bathymetryResult = ApiResult.NetworkError

        repository.getBathymetry(
            scanId = 1,
            token = "token"
        )

        api.bathymetryResult = ApiResult.Success(
            bathymetryDto()
        )

        repository.getBathymetry(
            scanId = 1,
            token = "token"
        )

        assertEquals(2, api.bathymetryCallCount)
    }


}
