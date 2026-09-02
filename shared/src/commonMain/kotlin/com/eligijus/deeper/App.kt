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
import com.eligijus.deeper.di.repositoryModule
import com.eligijus.deeper.di.useCaseModule
import com.eligijus.deeper.domain.repository.AuthRepository
import com.eligijus.deeper.domain.repository.BathymetryRepository
import com.eligijus.deeper.domain.request.BathymetryRequestOutcome
import com.eligijus.deeper.domain.request.LoginRequestOutcome
import com.eligijus.deeper.domain.usecase.LoginUseCase
import org.jetbrains.compose.resources.painterResource
import deeper.shared.generated.resources.Res
import deeper.shared.generated.resources.compose_multiplatform
import kotlinx.coroutines.launch
import org.koin.compose.KoinApplication
import org.koin.compose.koinInject
import org.koin.core.KoinApplication
import org.koin.dsl.koinConfiguration
import org.koin.dsl.module

@Composable
@Preview
fun App() {

    KoinApplication(configuration = koinConfiguration(declaration = {
        modules(
            networkModule,
            repositoryModule,
            useCaseModule
        )
    }), content = {
        MaterialTheme {
            val scope = rememberCoroutineScope()
            var showContent by remember { mutableStateOf(false) }
            val client = remember {
                HttpClientFactory.create()
            }
            val api: DeeperApi = DeeperApi(client)
            val authRepository: AuthRepository = AuthRepository(api)
            val bathRepository: BathymetryRepository = BathymetryRepository(api)
            Column(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .safeContentPadding()
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Button(onClick = { showContent = !showContent }) {
                    Text("Click me!")
                }
                Button(onClick = {
                    scope.launch {
                        val resAuth = authRepository.login("deeperangler@gmail.com", "Deeper10899")
                        when (resAuth) {
                            is LoginRequestOutcome.Success -> {
                                val token = resAuth.result.token
                                println(resAuth.result.scans)
                                val scan = resAuth.result.scans.firstOrNull()

                                if (scan == null) {
                                    println("No scans found")
                                    return@launch
                                }

                                println("Login successful")
                                println("Token: $token")
                                println("Scan ID: ${scan.id}")

                                when (
                                    val bathymetryOutcome =
                                        bathRepository.getBathymetry(
                                            scanId = scan.id,
                                            token = token
                                        )
                                ) {
                                    is BathymetryRequestOutcome.Success -> {
                                        println("Bathymetry request successful")
                                        println(bathymetryOutcome.result)
                                        println(bathymetryOutcome.result.features)

                                    }

                                    is BathymetryRequestOutcome.Failure -> {
                                        println(
                                            "Bathymetry request failed: ${bathymetryOutcome.error}"
                                        )
                                    }

                                }

                            }

                            is LoginRequestOutcome.Failure -> {
                                println(
                                    "Login failed: ${resAuth.error}"
                                )
                            }
                        }

                    }
                }) {
                    Text("Test KTOr")
                }

                AnimatedVisibility(showContent) {
                    val greeting = remember { Greeting().greet() }
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Image(painterResource(Res.drawable.compose_multiplatform), null)
                        Text("Compose: $greeting")
                    }
                }
            }
        }
    })
}
