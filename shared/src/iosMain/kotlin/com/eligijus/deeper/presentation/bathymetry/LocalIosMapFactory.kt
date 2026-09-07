package com.eligijus.deeper.presentation.bathymetry

import androidx.compose.runtime.staticCompositionLocalOf

val LocalIosMapFactory = staticCompositionLocalOf<IosMapFactory> {
    error("IosMapFactory was not provided")
}