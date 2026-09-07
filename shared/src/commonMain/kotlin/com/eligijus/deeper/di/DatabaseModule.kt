package com.eligijus.deeper.di

import com.eligijus.deeper.data.local.BathymetryCacheDao
import com.eligijus.deeper.data.local.DeeperDatabase
import com.eligijus.deeper.data.local.buildDatabase
import org.koin.dsl.module


val databaseModule = module {

    single<DeeperDatabase> {
        buildDatabase()
    }

    single<BathymetryCacheDao> {
        get<DeeperDatabase>()
            .bathymetryCacheDao()
    }
}