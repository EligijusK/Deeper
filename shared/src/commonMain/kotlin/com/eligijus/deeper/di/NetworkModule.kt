package com.eligijus.deeper.di

import com.eligijus.deeper.data.remote.DeeperApiInterface
import com.eligijus.deeper.data.remote.DeeperApi
import com.eligijus.deeper.data.remote.HttpClientFactory
import com.eligijus.deeper.domain.repository.ScanRepository
import com.eligijus.deeper.domain.repository.`interface`.ScanRepositoryInterface
import io.ktor.client.HttpClient
import org.koin.dsl.module

val networkModule = module {

    single<HttpClient> {
        HttpClientFactory.create()
    }

    single<DeeperApiInterface> {
        DeeperApi(
            client = get()
        )
    }

    single<ScanRepositoryInterface> {
        ScanRepository(
            deeperApi = get()
        )
    }
}