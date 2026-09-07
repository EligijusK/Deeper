package com.eligijus.deeper.data.local

import androidx.room3.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver

fun buildDatabase(
    builder: RoomDatabase.Builder<DeeperDatabase>
): DeeperDatabase {
    return builder
        .setDriver(
            BundledSQLiteDriver()
        )
        .build()
}