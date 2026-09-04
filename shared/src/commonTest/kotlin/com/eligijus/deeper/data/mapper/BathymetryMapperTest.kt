package com.eligijus.deeper.data.mapper

import com.eligijus.deeper.data.remote.ApiResult
import com.eligijus.deeper.data.remote.DeeperApi
import com.eligijus.deeper.data.remote.createTestClient
import com.eligijus.deeper.data.remote.dto.bathymetry.BathymetryResponseDto
import com.eligijus.deeper.data.remote.dto.bathymetry.FeatureCollectionDto
import com.eligijus.deeper.data.remote.dto.bathymetry.FeatureDto
import com.eligijus.deeper.data.remote.dto.bathymetry.GeometryDto
import com.eligijus.deeper.data.remote.dto.bathymetry.PropertiesDto
import com.eligijus.deeper.data.remote.dto.bathymetry.ScanGeoDataDto
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotNull

class BathymetryMapperTest {

    @Test
    fun bathymetryMapper_mapsCoordinatesCorrectly() {

        val dto = BathymetryResponseDto(
            bathymetry = FeatureCollectionDto(
                type = "FeatureCollection",
                bbox = listOf(
                    54.6967280,
                    25.3042785,
                    54.6969794,
                    25.3047285
                ),
                features = listOf(
                    FeatureDto(
                        type = "Feature",
                        properties = PropertiesDto(
                            depth = 1.666666,
                            id = "test-feature"
                        ),
                        geometry = GeometryDto(
                            type = "Polygon",
                            bbox =  listOf(
                                54.6967280,
                                25.3042785,
                                54.6969794,
                                25.3047285
                            ),
                            coordinates = listOf(
                                listOf(
                                    listOf(
                                        25.3042785,
                                        54.6969677,
                                        1.666666
                                    ),
                                    listOf(
                                        25.3044794,
                                        54.6969677,
                                        1.666666
                                    ),
                                    listOf(
                                        25.3045141,
                                        54.6969794,
                                        1.666666
                                    )
                                )
                            )
                        )
                    )
                )
            ),
            scansGeoData = emptyList()
        )

        val result = dto.toDomain()

        assertEquals(
            1,
            result.features.size
        )

        val feature = result.features.first()

        assertEquals(
            "test-feature",
            feature.id
        )

        assertEquals(
            1.666666,
            feature.depth,
            absoluteTolerance = 0.000001
        )

        val point = feature.geometry.coordinates.first()

        assertEquals(
            54.6969677,
            point.latitude,
            absoluteTolerance = 0.0000001
        )

        assertEquals(
            25.3042785,
            point.longitude,
            absoluteTolerance = 0.0000001
        )

        val bbox = feature.geometry.boundingBox

        assertNotNull(bbox)

        assertEquals(54.6967280, bbox.minLatitude, 0.0000001)
        assertEquals(25.3042785, bbox.minLongitude, 0.0000001)
        assertEquals(54.6969794, bbox.maxLatitude, 0.0000001)
        assertEquals(25.3047285, bbox.maxLongitude, 0.0000001)
    }

    @Test
    fun scanGeoDataIsMappedCorrectly() {
        val dto = BathymetryResponseDto(
            bathymetry = FeatureCollectionDto(
                type = "FeatureCollection",
                bbox = listOf(
                    54.6967280,
                    25.3042785,
                    54.6969794,
                    25.3047285
                ),
                features = emptyList()
            ),
            scansGeoData = listOf(
                ScanGeoDataDto(
                    id = 2434155,
                    mode = 1,
                    coordinates = listOf(
                        listOf(
                            25.511019,
                            55.06153
                        ),
                        listOf(
                            25.51068,
                            55.061863
                        )
                    ),
                    polygons = listOf(
                        listOf(
                            listOf(
                                25.3042567,
                                54.6974405
                            ),
                            listOf(
                                25.304138,
                                54.6973214
                            ),
                            listOf(
                                25.3042567,
                                54.6972024
                            )
                        )
                    ),
                    startLocation = listOf(
                        25.511019,
                        55.06153
                    ),
                    size = 3443.0
                )
            )
        )

        val result = dto.toDomain()

        assertEquals(
            expected = 1,
            actual = result.scansGeoData.size
        )

        val scanGeoData = result.scansGeoData.first()

        assertEquals(
            expected = 2434155,
            actual = scanGeoData.id
        )

        assertEquals(
            expected = 1,
            actual = scanGeoData.mode
        )

        assertEquals(
            expected = 3443.0,
            actual = scanGeoData.size
        )

        assertEquals(
            expected = 2,
            actual = scanGeoData.coordinates.size
        )

        val coordinate = scanGeoData.coordinates.first()

        assertEquals(
            expected = 55.06153,
            actual = coordinate.latitude,
            absoluteTolerance = 0.0000001
        )

        assertEquals(
            expected = 25.511019,
            actual = coordinate.longitude,
            absoluteTolerance = 0.0000001
        )

        assertEquals(
            expected = 1,
            actual = scanGeoData.polygons.size
        )

        assertEquals(
            expected = 3,
            actual = scanGeoData.polygons.first().size
        )

        val startLocation = scanGeoData.startLocation

        assertNotNull(startLocation)

        assertEquals(
            expected = 55.06153,
            actual = startLocation.latitude,
            absoluteTolerance = 0.0000001
        )

        assertEquals(
            expected = 25.511019,
            actual = startLocation.longitude,
            absoluteTolerance = 0.0000001
        )
    }

    @Test
    fun scanGeoDataHandlesNullableGeometry() {
        val dto = BathymetryResponseDto(
            bathymetry = FeatureCollectionDto(
                type = "FeatureCollection",
                bbox = listOf(
                    54.0,
                    25.0,
                    55.0,
                    26.0
                ),
                features = emptyList()
            ),
            scansGeoData = listOf(
                ScanGeoDataDto(
                    id = 1,
                    mode = 0,
                    coordinates = null,
                    polygons = null,
                    startLocation = null,
                    size = null
                )
            )
        )

        val result = dto.toDomain()

        val data = result.scansGeoData.first()

        assertEquals(
            expected = emptyList(),
            actual = data.coordinates
        )

        assertEquals(
            expected = emptyList(),
            actual = data.polygons
        )

        assertEquals(
            expected = null,
            actual = data.startLocation
        )

        assertEquals(
            expected = null,
            actual = data.size
        )
    }

    @Test
    fun getBathymetryReturnsSuccessWhenServerReturns200() = runTest {

        val engine = MockEngine {
            respond(
                content = """
                {
                  "bathymetry": {
                    "type": "FeatureCollection",
                    "bbox": [
                      54.6967280,
                      25.3042785,
                      54.6969794,
                      25.3047285
                    ],
                    "features": [
                      {
                        "type": "Feature",
                        "properties": {
                          "depth": 1.666666666665,
                          "id": "test-feature"
                        },
                        "geometry": {
                          "type": "Polygon",
                          "bbox": [
                            54.6967280,
                            25.3042785,
                            54.6969794,
                            25.3047285
                          ],
                          "coordinates": [
                            [
                              [25.3042785, 54.6969677, 1.666666666665],
                              [25.3044794, 54.6969677, 1.666666666665],
                              [25.3045141, 54.6969794, 1.666666666665]
                            ]
                          ]
                        }
                      }
                    ]
                  },
                  "scansGeoData": [
                    {
                      "id": 2434155,
                      "mode": 1,
                      "coordinates": [
                        [25.511019, 55.06153]
                      ],
                      "polygons": null,
                      "startLocation": [
                        25.511019,
                        55.06153
                      ],
                      "size": "2"
                    }
                  ]
                }
            """.trimIndent(),
                status = HttpStatusCode.OK,
                headers = headersOf(
                    HttpHeaders.ContentType,
                    ContentType.Application.Json.toString()
                )
            )
        }

        val client = createTestClient(engine)
        val api = DeeperApi(client)

        val result = api.getBathymetry(
            scanId = 2434155,
            token = "test-token"
        )

        val success =
            assertIs<ApiResult.Success<BathymetryResponseDto>>(result)

        assertEquals(
            expected = 1,
            actual = success.data.bathymetry.features.size
        )

        assertEquals(
            expected = "test-feature",
            actual = success.data.bathymetry
                .features
                .first()
                .properties
                .id
        )

        assertEquals(
            expected = 1,
            actual = success.data.scansGeoData.size
        )

        client.close()
    }

    @Test
    fun getBathymetryReturnsUnauthorizedWhenServerReturns401() = runTest {
        val engine = MockEngine {
            respond(
                content = "",
                status = HttpStatusCode.Unauthorized
            )
        }

        val api = DeeperApi(
            createTestClient(engine)
        )

        val result = api.getBathymetry(
            scanId = 1,
            token = "invalid-token"
        )

        assertEquals(
            ApiResult.Unauthorized,
            result
        )
    }

    @Test
    fun getBathymetryReturnsServerErrorWhenServerReturns500() = runTest {
        val engine = MockEngine {
            respond(
                content = "",
                status = HttpStatusCode.InternalServerError
            )
        }

        val api = DeeperApi(
            createTestClient(engine)
        )

        val result = api.getBathymetry(
            scanId = 1,
            token = "test-token"
        )

        assertEquals(
            ApiResult.ServerError,
            result
        )
    }

    @Test
    fun loginReturnsServerErrorWhenServerReturns403() = runTest {

        val engine = MockEngine {
            respond(
                content = "",
                status = HttpStatusCode.Forbidden
            )
        }

        val api = DeeperApi(
            createTestClient(engine)
        )


        val result = api.getBathymetry(
            scanId = 1,
            token = "test-token"
        )

        assertEquals(
            ApiResult.Forbidden,
            result
        )
    }

}