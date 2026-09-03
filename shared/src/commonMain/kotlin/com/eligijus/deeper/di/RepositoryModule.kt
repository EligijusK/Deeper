package com.eligijus.deeper.di


import com.eligijus.deeper.domain.repository.AuthRepository
import com.eligijus.deeper.domain.repository.ScanRepository
import com.eligijus.deeper.domain.repository.`interface`.ScanRepositoryInterface
import com.eligijus.deeper.domain.repository.`interface`.AuthRepositoryInterface
import org.koin.dsl.module

val repositoryModule = module {

    single<AuthRepositoryInterface> {
        AuthRepository(
            api = get()
        )
    }

    single<ScanRepositoryInterface> {
        ScanRepository(
            deeperApi = get()
        )
    }

}