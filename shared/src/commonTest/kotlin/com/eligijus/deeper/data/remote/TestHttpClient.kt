package com.eligijus.deeper.data.remote

import com.eligijus.deeper.data.remote.dto.login.LoginResponseDto
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.MockEngine.Companion.invoke
import io.ktor.client.engine.mock.respond
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class TestHttpClient {

    @Test
    fun loginReturnsSuccessWhenServerReturns200() = runTest {

        val engine = MockEngine {
            respond(
                content = """
                    {
                        "login": {
                            "token": "test-token",
                            "userId": 123,
                            "validated": true
                        },
                        "scans": [
                            {
                                "id": 2434155,
                                "lat": 55.06153,
                                "lon": 25.511019,
                                "name": null,
                                "date": null,
                                "scanPoints": 9,
                                "mode": 1
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

        val result = api.login(
            email = "test@test.com",
            password = "password"
        )

        val success = assertIs<ApiResult.Success<LoginResponseDto>>(
            result
        )

        assertEquals(
            "test-token",
            success.data.login.token
        )

        assertEquals(
            123,
            success.data.login.userId
        )

        assertEquals(
            1,
            success.data.scans.size
        )

        assertEquals(
            null,
            success.data.scans.first().name
        )

        client.close()
    }

    @Test
    fun loginReturnsUnauthorizedWhenServerReturns401() = runTest {

        val engine = MockEngine {
            respond(
                content = """
                {
                    "errorCode": 1
                }
            """.trimIndent(),
                status = HttpStatusCode.Unauthorized,
                headers = headersOf(
                    HttpHeaders.ContentType,
                    ContentType.Application.Json.toString()
                )
            )
        }

        val api = DeeperApi(
            createTestClient(engine)
        )

        val result = api.login(
            email = "wrong@test.com",
            password = "wrong-password"
        )

        assertEquals(
            ApiResult.Unauthorized,
            result
        )
    }

    @Test
    fun loginReturnsServerErrorWhenServerReturns500() = runTest {

        val engine = MockEngine {
            respond(
                content = "",
                status = HttpStatusCode.InternalServerError
            )
        }

        val api = DeeperApi(
            createTestClient(engine)
        )

        val result = api.login(
            email = "test@test.com",
            password = "password"
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

        val result = api.login(
            email = "test@test.com",
            password = "password"
        )

        assertEquals(
            ApiResult.Forbidden,
            result
        )
    }
}
