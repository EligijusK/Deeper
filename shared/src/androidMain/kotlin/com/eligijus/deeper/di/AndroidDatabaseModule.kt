package com.eligijus.deeper.di

import androidx.room3.Room
import com.eligijus.deeper.data.local.BathymetryCacheDao
import com.eligijus.deeper.data.local.DeeperDatabase
import com.eligijus.deeper.data.local.buildDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

actual fun initModule(): Module {

    return module {

        single<DeeperDatabase> {
            val context = androidContext()

            buildDatabase(
                Room.databaseBuilder<DeeperDatabase>(
                    context = context,
                    name = "deeper.db"
                )
            )
        }

        single<BathymetryCacheDao> {
            get<DeeperDatabase>()
                .bathymetryCacheDao()
        }

    }
}