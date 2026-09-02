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
import com.eligijus.deeper.data.remote.DeeperApi
import com.eligijus.deeper.data.remote.HttpClientFactory
import com.eligijus.deeper.domain.repository.AuthRepositoryInterfaceImpl
import org.jetbrains.compose.resources.painterResource
import deeper.shared.generated.resources.Res
import deeper.shared.generated.resources.compose_multiplatform
import kotlinx.coroutines.launch

@Composable
@Preview
fun App() {
    MaterialTheme {
        val scope = rememberCoroutineScope()
        var showContent by remember { mutableStateOf(false) }
        val client = remember {
            HttpClientFactory.create()
        }
        val api: DeeperApi = DeeperApi(client)
        val authRepository: AuthRepositoryInterfaceImpl = AuthRepositoryInterfaceImpl(api)
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
            Button(onClick = {  scope.launch {
                val res = authRepository.login("deeperangle@gmail.com", "Deeper10899")
                println(res)

            } }) {
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
}