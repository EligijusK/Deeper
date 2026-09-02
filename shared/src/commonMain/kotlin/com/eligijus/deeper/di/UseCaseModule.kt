package com.eligijus.deeper.di

import com.eligijus.deeper.domain.usecase.BathymetryUseCase
import com.eligijus.deeper.domain.usecase.LoginUseCase
import org.koin.dsl.module

val useCaseModule = module {

    factory {
        LoginUseCase(
            authRepository = get()
        )
    }

    factory {
        BathymetryUseCase(
            scanRepository = get()
        )
    }
}