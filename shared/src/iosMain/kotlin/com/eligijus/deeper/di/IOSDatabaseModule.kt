package com.eligijus.deeper.di

import androidx.room3.Room
import com.eligijus.deeper.data.local.BathymetryCacheDao
import com.eligijus.deeper.data.local.DeeperDatabase
import com.eligijus.deeper.data.local.buildDatabase
import kotlinx.cinterop.ExperimentalForeignApi
import org.koin.core.module.Module
import org.koin.dsl.module
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

@OptIn(ExperimentalForeignApi::class)
actual fun initModule(): Module {
    return module {

        single<DeeperDatabase> {

            val directory =
                NSFileManager.defaultManager.URLForDirectory(
                    directory = NSDocumentDirectory,
                    inDomain = NSUserDomainMask,
                    appropriateForURL = null,
                    create = true,
                    error = null
                )

            val path =
                requireNotNull(directory?.path) +
                        "/deeper.db"

            buildDatabase(
                Room.databaseBuilder<DeeperDatabase>(
                    name = path
                )
            )
        }

        single<BathymetryCacheDao> {
            get<DeeperDatabase>()
                .bathymetryCacheDao()
        }

    }
}