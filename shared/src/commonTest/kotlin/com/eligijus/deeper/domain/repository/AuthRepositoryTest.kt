package com.eligijus.deeper.domain.repository

import com.eligijus.deeper.data.remote.ApiResult
import com.eligijus.deeper.data.remote.dto.login.LoginDto
import com.eligijus.deeper.data.remote.dto.login.LoginResponseDto
import com.eligijus.deeper.data.remote.dto.login.ScanDto
import com.eligijus.deeper.domain.request.LoginRequestOutcome
import com.eligijus.deeper.domain.request.RequestError
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class AuthRepositoryTest {
    private lateinit var fakeApi: FakeDeeperApi
    private lateinit var repository: AuthRepository

    @BeforeTest
    fun setup() {
        fakeApi = FakeDeeperApi()

        repository = AuthRepository(
            api = fakeApi
        )
    }

    @Test
    fun loginReturnsSuccessWhenApiReturnsSuccess() = runTest {

        fakeApi.loginResult = ApiResult.Success(
            LoginResponseDto(
                login = LoginDto(
                    token = "test-token",
                    userId = 123,
                    validated = true
                ),
                scans = listOf(
                    ScanDto(
                        id = 2434155,
                        lat = 55.06153,
                        lon = 25.511019,
                        name = "Test scan",
                        date = null,
                        scanPoints = 9,
                        mode = 1
                    )
                )
            )
        )

        val result = repository.login(
            email = "test@test.com",
            password = "password"
        )

        val success =
            assertIs<LoginRequestOutcome.Success>(result)

        assertEquals(
            expected = "test-token",
            actual = success.result.token
        )

        assertEquals(
            expected = 123,
            actual = success.result.userId
        )

        assertEquals(
            expected = 1,
            actual = success.result.scans.size
        )

        val scan = success.result.scans.first()

        assertEquals(
            expected = 55.06153,
            actual = scan.latitude,
            absoluteTolerance = 0.000001
        )

        assertEquals(
            expected = 25.511019,
            actual = scan.longitude,
            absoluteTolerance = 0.000001
        )
    }

    @Test
    fun loginReturnsInvalidCredentialsWhenApiReturnsUnauthorized() = runTest {

        fakeApi.loginResult = ApiResult.Unauthorized

        val result = repository.login(
            email = "wrong@test.com",
            password = "wrong"
        )

        val failure =
            assertIs<LoginRequestOutcome.Failure>(result)

        assertEquals(
            expected = RequestError.InvalidCredentials,
            actual = failure.error
        )
    }

    @Test
    fun loginReturnsServerErrorWhenApiReturnsServerError() = runTest {

        fakeApi.loginResult = ApiResult.ServerError

        val result = repository.login(
            email = "test@test.com",
            password = "password"
        )

        val failure =
            assertIs<LoginRequestOutcome.Failure>(result)

        assertEquals(
            expected = RequestError.ServerError,
            actual = failure.error
        )
    }
}