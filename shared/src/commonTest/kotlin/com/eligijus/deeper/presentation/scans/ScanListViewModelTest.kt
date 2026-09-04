package com.eligijus.deeper.presentation.scans

import com.eligijus.deeper.domain.model.BathymetryAvailability
import com.eligijus.deeper.domain.model.Scan
import com.eligijus.deeper.domain.usecase.GetBathymetryAvailabilityUseCase
import kotlin.test.Test
import kotlin.test.assertEquals

class ScanListViewModelTest {
    private fun scan(
        id: Long
    ): Scan {
        return Scan(
            id = id,
            latitude = 54.0,
            longitude = 23.0,
            name = "Scan $id",
            date = "2026-09-04",
            scanPoints = 10,
            mode = 1
        )
    }
    @Test
    fun `refresh availability updates state for all scans`() {
        val repository = FakeScanRepository(
            availability = mapOf(
                1L to BathymetryAvailability.AVAILABLE,
                2L to BathymetryAvailability.NOT_AVAILABLE
            )
        )

        val viewModel = ScanListViewModel(
            getBathymetryAvailabilityUseCase =
                GetBathymetryAvailabilityUseCase(repository)
        )

        val scans = listOf(
            scan(id = 1L),
            scan(id = 2L),
            scan(id = 3L)
        )

        viewModel.refreshAvailability(scans)

        val state = viewModel.uiState.value

        assertEquals(
            BathymetryAvailability.AVAILABLE,
            state.availability[1L]
        )

        assertEquals(
            BathymetryAvailability.NOT_AVAILABLE,
            state.availability[2L]
        )

        assertEquals(
            BathymetryAvailability.UNKNOWN,
            state.availability[3L]
        )
    }

    @Test
    fun `initial state contains no availability values`() {
        val viewModel = ScanListViewModel(
            getBathymetryAvailabilityUseCase =
                GetBathymetryAvailabilityUseCase(
                    FakeScanRepository()
                )
        )

        assertEquals(
            emptyMap(),
            viewModel.uiState.value.availability
        )
    }
}