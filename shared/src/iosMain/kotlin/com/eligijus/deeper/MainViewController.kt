package com.eligijus.deeper

import androidx.compose.ui.window.ComposeUIViewController
import androidx.compose.runtime.CompositionLocalProvider
import com.eligijus.deeper.presentation.bathymetry.IosMapFactory
import com.eligijus.deeper.presentation.bathymetry.LocalIosMapFactory

fun MainViewController(
    mapFactory: IosMapFactory
) = ComposeUIViewController {
    CompositionLocalProvider(
        LocalIosMapFactory provides mapFactory
    ) {
        App()
    }
}