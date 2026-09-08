package com.eligijus.deeper.di

import com.eligijus.deeper.data.local.BathymetryCacheDao
import com.eligijus.deeper.data.local.DeeperDatabase
import com.eligijus.deeper.data.local.buildDatabase
import org.koin.core.module.Module
import org.koin.dsl.module

expect fun initModule(): Module

val databaseModule = initModule()