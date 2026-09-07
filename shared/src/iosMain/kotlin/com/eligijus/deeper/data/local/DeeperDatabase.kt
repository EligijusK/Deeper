package com.eligijus.deeper.data.local

import androidx.room3.Room
import androidx.room3.RoomDatabase

import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

actual fun createBuilder(): RoomDatabase.Builder<DeeperDatabase> {
    return Room.databaseBuilder<DeeperDatabase>(
        name = "deeper.db"
    )
}
