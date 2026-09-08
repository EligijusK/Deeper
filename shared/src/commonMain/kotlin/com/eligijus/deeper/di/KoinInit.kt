package com.eligijus.deeper.di

import org.koin.core.KoinApplication
import org.koin.dsl.KoinConfiguration
import org.koin.dsl.koinConfiguration

fun koinInitialization(): KoinConfiguration {

    return koinConfiguration(declaration = {
        modules(
            databaseModule,
            networkModule,
            repositoryModule,
            useCaseModule,
            presentationModule
        )
    })
}

