package com.eligijus.deeper


import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import com.eligijus.deeper.di.networkModule
import com.eligijus.deeper.di.presentationModule
import com.eligijus.deeper.di.repositoryModule
import com.eligijus.deeper.di.useCaseModule
import com.eligijus.deeper.domain.model.LoginResult
import com.eligijus.deeper.presentation.bathymetry.BathymetryRoute
import com.eligijus.deeper.presentation.bathymetry.BathymetryViewModel
import com.eligijus.deeper.presentation.login.LoginEvent
import com.eligijus.deeper.presentation.login.LoginScreen
import com.eligijus.deeper.presentation.login.LoginViewModel
import com.eligijus.deeper.presentation.scans.ScanListRoute
import org.koin.compose.KoinApplication
import org.koin.compose.viewmodel.koinViewModel
import org.koin.dsl.koinConfiguration


@Composable
@Preview
fun App() {

    var route by remember {
        mutableStateOf<AppRoute>(AppRoute.Login)
    }
    var loginResult by remember {
        mutableStateOf<LoginResult?>(null)
    }
    KoinApplication(configuration = koinConfiguration(declaration = {
        modules(
            networkModule,
            repositoryModule,
            useCaseModule,
            presentationModule
        )
    }), content = {

        val viewModel = koinViewModel<LoginViewModel>()
        val state by viewModel.uiState.collectAsState()

        LaunchedEffect(viewModel) {
            viewModel.events.collect { event ->
                when (event) {
                    is LoginEvent.Success -> {
                        loginResult = event.result
                        route = AppRoute.ScanList(
                            loginResult = event.result
                        )
                    }
                }
            }
        }

        when (val currentRoute = route) {

            AppRoute.Login -> {
                LoginScreen(
                    state = state,
                    onEmailChanged = viewModel::onEmailChanged,
                    onPasswordChanged = viewModel::onPasswordChanged,
                    onLoginClicked = viewModel::login
                )
            }

            is AppRoute.ScanList -> {
                ScanListRoute(
                    scans = currentRoute.loginResult.scans,
                    onScanClick = { scan ->
                        route = AppRoute.Bathymetry(
                            scan = scan, // Scan(2434165, 54.70320881442833, 25.157326583218417, "", null, 231, 2), // scan,
                            token = currentRoute.loginResult.token //"sZVBFabi7jtI1RV7qIOBrXdotIIXuDIq" // currentRoute.loginResult.token
                        )
                    }
                )
            }

            is AppRoute.Bathymetry -> {

                val bathymetryViewModel = koinViewModel<BathymetryViewModel>()

                LaunchedEffect(
                    currentRoute.scan.id,
                    currentRoute.token
                ) {
                    bathymetryViewModel.loadBathymetry(
                        scanId = currentRoute.scan.id, //2434165,
                        token = currentRoute.token //"sZVBFabi7jtI1RV7qIOBrXdotIIXuDIq"
                    )
                }

                BathymetryRoute(
                    scan = currentRoute.scan,
                    token = currentRoute.token,
                    onBackClick = {
                        loginResult?.let { result ->
                            route = AppRoute.ScanList(
                                loginResult = result
                            )
                        }
                    }
                )
            }

        }
    })
}
