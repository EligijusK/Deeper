package com.eligijus.deeper.domain.request

import com.eligijus.deeper.domain.model.Bathymetry

sealed interface BathymetryRequestOutcome {

    data class Success(
        val result: Bathymetry
    ) : BathymetryRequestOutcome

    data class Failure(
        val error: RequestError
    ) : BathymetryRequestOutcome
}