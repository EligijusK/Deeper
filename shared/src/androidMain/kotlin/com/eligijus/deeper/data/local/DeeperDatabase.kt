package com.eligijus.deeper.data.local

import android.content.Context
import androidx.room3.Room
import androidx.room3.RoomDatabase


actual fun createBuilder():
        RoomDatabase.Builder<DeeperDatabase> {

    return Room.databaseBuilder<DeeperDatabase>(
        name = "deeper.db"
    )
}
