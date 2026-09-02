package com.eligijus.deeper.di

import com.eligijus.deeper.data.remote.DeeperApiInterface
import com.eligijus.deeper.data.remote.DeeperApi
import com.eligijus.deeper.data.remote.HttpClientFactory
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
}