package com.eligijus.deeper.di

import com.eligijus.deeper.presentation.login.LoginViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module


val presentationModule = module {

    viewModel {
        LoginViewModel(
            loginUseCase = get()
        )
    }
}