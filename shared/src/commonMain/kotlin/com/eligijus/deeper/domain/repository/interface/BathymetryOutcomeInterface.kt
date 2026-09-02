package com.eligijus.deeper.domain.repository.`interface`

import com.eligijus.deeper.domain.model.Bathymetry

sealed interface BathymetryOutcomeInterface {

    data class Success(
        val data: Bathymetry
    ) : BathymetryOutcomeInterface

//    data class Failure(
//        val error: BathymetryError
//    ) : BathymetryOutcome
}