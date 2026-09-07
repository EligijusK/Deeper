package com.eligijus.deeper.data.local

import androidx.room3.ConstructedBy
import androidx.room3.Database
import androidx.room3.RoomDatabase
import androidx.room3.RoomDatabaseConstructor

    @Database(
        entities = [
            BathymetryCacheEntity::class
        ],
        version = 1,
        exportSchema = true
    )
    @ConstructedBy(DeeperDatabaseConstructor::class)
    abstract class DeeperDatabase : RoomDatabase() {

        abstract fun bathymetryCacheDao(): BathymetryCacheDao
    }

    @Suppress("KotlinNoActualForExpect")
    expect object DeeperDatabaseConstructor :
        RoomDatabaseConstructor<DeeperDatabase> {

        override fun initialize(): DeeperDatabase
    }