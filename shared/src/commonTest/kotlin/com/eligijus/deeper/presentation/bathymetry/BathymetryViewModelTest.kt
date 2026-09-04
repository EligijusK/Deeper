package com.eligijus.deeper.presentation.bathymetry

import com.eligijus.deeper.data.remote.ApiResult
import com.eligijus.deeper.domain.model.Bathymetry
import com.eligijus.deeper.domain.model.BathymetryGeometry
import com.eligijus.deeper.domain.model.BathymetryFeature
import com.eligijus.deeper.domain.model.BoundingBox
import com.eligijus.deeper.domain.usecase.BathymetryUseCase
import com.eligijus.deeper.domain.request.BathymetryRequestOutcome
import com.eligijus.deeper.domain.request.RequestError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull

@OptIn(ExperimentalCoroutinesApi::class)
class BathymetryViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `successful request updates bathymetry state`() = runTest {
        val bathymetry = validBathymetry()

        val repository = FakeBathymetryRepository(
            result = BathymetryRequestOutcome.Success(
                result = bathymetry
            )
        )

        val viewModel = BathymetryViewModel(
            bathymetryUseCase = BathymetryUseCase(repository)
        )

        viewModel.loadBathymetry(
            scanId = 1L,
            token = "token"
        )

        advanceUntilIdle()

        val state = viewModel.uiState.value

        assertFalse(state.isLoading)
        assertEquals(bathymetry, state.bathymetry)
        assertNull(state.errorMessage)
    }

    @Test
    fun `failed request shows error and clears bathymetry`() = runTest {
        val repository = FakeBathymetryRepository(
            result = BathymetryRequestOutcome.Failure(
                RequestError.NetworkError
            )
        )

        val viewModel = BathymetryViewModel(
            bathymetryUseCase = BathymetryUseCase(repository)
        )

        viewModel.loadBathymetry(
            scanId = 1L,
            token = "token"
        )

        advanceUntilIdle()

        val state = viewModel.uiState.value

        assertFalse(state.isLoading)
        assertNull(state.bathymetry)
        assertEquals(
            "Unable to connect. Check your internet connection.",
            state.errorMessage
        )
    }

    @Test
    fun `initial state contains no bathymetry`() {
        val repository = FakeBathymetryRepository(
            result = BathymetryRequestOutcome.Failure(
                RequestError.UnknownError
            )
        )

        val viewModel = BathymetryViewModel(
            bathymetryUseCase = BathymetryUseCase(repository)
        )

        val state = viewModel.uiState.value

        assertFalse(state.isLoading)
        assertNull(state.bathymetry)
        assertNull(state.errorMessage)
    }

    private fun validBathymetry(): Bathymetry {
        return Bathymetry(
            boundingBox = BoundingBox(
                minLatitude = 54.0,
                minLongitude = 23.0,
                maxLatitude = 55.0,
                maxLongitude = 24.0
            ),
            features = listOf(
                BathymetryFeature(
                    id = "feature-1",
                    depth = 2.0,
                    geometry = BathymetryGeometry(
                        boundingBox = null,
                        coordinates = emptyList()
                    )
                )
            ),
            scansGeoData = emptyList()
        )
    }
}