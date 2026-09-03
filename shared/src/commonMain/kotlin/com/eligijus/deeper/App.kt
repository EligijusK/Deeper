package com.eligijus.deeper

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.eligijus.deeper.data.remote.ApiResult
import com.eligijus.deeper.data.remote.DeeperApi
import com.eligijus.deeper.data.remote.HttpClientFactory
import com.eligijus.deeper.di.networkModule
import com.eligijus.deeper.di.presentationModule
import com.eligijus.deeper.di.repositoryModule
import com.eligijus.deeper.di.useCaseModule
import com.eligijus.deeper.domain.model.LoginResult
import com.eligijus.deeper.domain.repository.AuthRepository
import com.eligijus.deeper.domain.repository.BathymetryRepository
import com.eligijus.deeper.domain.request.BathymetryRequestOutcome
import com.eligijus.deeper.domain.request.LoginRequestOutcome
import com.eligijus.deeper.domain.usecase.LoginUseCase
import com.eligijus.deeper.presentation.bathymetry.BathymetryScreen
import com.eligijus.deeper.presentation.bathymetry.BathymetryViewModel
import com.eligijus.deeper.presentation.login.LoginEvent
import com.eligijus.deeper.presentation.login.LoginScreen
import com.eligijus.deeper.presentation.login.LoginViewModel
import com.eligijus.deeper.presentation.scans.ScanListScreen
import org.jetbrains.compose.resources.painterResource
import deeper.shared.generated.resources.Res
import deeper.shared.generated.resources.compose_multiplatform
import kotlinx.coroutines.launch
import org.koin.compose.KoinApplication
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.KoinApplication
import org.koin.dsl.koinConfiguration
import org.koin.dsl.module

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
                    ScanListScreen(
                        scans = currentRoute.loginResult.scans,
                        onScanClick = { scan ->
                            route = AppRoute.Bathymetry(
                                scan = scan,
                                token = currentRoute.loginResult.token
                            )
                        }
                    )
                }

            is AppRoute.Bathymetry -> {

                val bathymetryViewModel = koinViewModel<BathymetryViewModel>()

                val bathymetryState by bathymetryViewModel.uiState.collectAsState()

                LaunchedEffect(
                    currentRoute.scan.id,
                    currentRoute.token
                ) {
                    bathymetryViewModel.loadBathymetry(
                        scanId = currentRoute.scan.id,
                        token = currentRoute.token
                    )
                }

                BathymetryScreen(
                    scan = currentRoute.scan,
                    state = bathymetryState,
                    onBackClick = {
                        loginResult?.let { result ->
                            route = AppRoute.ScanList(
                                loginResult = result
                            )
                        }
                    }
                )
            }
//
//                is AppRoute.Bathymetry -> {
//                    BathymetryScreen(
//                        scanId = currentRoute.scanId,
//                        token = currentRoute.token,
//                        onBack = {
//                            // we'll improve this
//                        }
//                    )
//                }
            is AppRoute.Bathymetry -> TODO()

        }
//        MaterialTheme {

//            val scope = rememberCoroutineScope()
//            var showContent by remember { mutableStateOf(false) }
//            val client = remember {
//                HttpClientFactory.create()
//            }
//            val api: DeeperApi = DeeperApi(client)
//            val authRepository: AuthRepository = AuthRepository(api)
//            val bathRepository: BathymetryRepository = BathymetryRepository(api)
//            Column(
//                modifier = Modifier
//                    .background(MaterialTheme.colorScheme.primaryContainer)
//                    .safeContentPadding()
//                    .fillMaxSize(),
//                horizontalAlignment = Alignment.CenterHorizontally,
//            ) {
//                Button(onClick = { showContent = !showContent }) {
//                    Text("Click me!")
//                }
//                Button(onClick = {
//                    scope.launch {
//                        val result = loginUseCase.invoke("deeperangler@gmail.com", "Deeper10899")
//                        when (result) {
//                            is LoginRequestOutcome.Success -> {
//                                val token = result.result.token
//                                println(result.result.scans)
//                                val scan = result.result.scans.firstOrNull()
//
//                                if (scan == null) {
//                                    println("No scans found")
//                                    return@launch
//                                }
//
//                                println("Login successful")
//                                println("Token: $token")
//                                println("Scan ID: ${scan.id}")
//
//                                when (
//                                    val bathymetryOutcome =
//                                        bathRepository.getBathymetry(
//                                            scanId = scan.id,
//                                            token = token
//                                        )
//                                ) {
//                                    is BathymetryRequestOutcome.Success -> {
//                                        println("Bathymetry request successful")
//                                        println(bathymetryOutcome.result)
//                                        println(bathymetryOutcome.result.features)
//
//                                    }
//
//                                    is BathymetryRequestOutcome.Failure -> {
//                                        println(
//                                            "Bathymetry request failed: ${bathymetryOutcome.error}"
//                                        )
//                                    }
//
//                                }
//
//                            }
//
//                            is LoginRequestOutcome.Failure -> {
//                                println(
//                                    "Login failed: ${result.error}"
//                                )
//                            }
//                        }
//
//                    }
//                }) {
//                    Text("Test KTOr")
//                }
//
//                AnimatedVisibility(showContent) {
//                    val greeting = remember { Greeting().greet() }
//                    Column(
//                        modifier = Modifier.fillMaxWidth(),
//                        horizontalAlignment = Alignment.CenterHorizontally,
//                    ) {
//                        Image(painterResource(Res.drawable.compose_multiplatform), null)
//                        Text("Compose: $greeting")
//                    }
//                }
//            }
//        }
    })
}
