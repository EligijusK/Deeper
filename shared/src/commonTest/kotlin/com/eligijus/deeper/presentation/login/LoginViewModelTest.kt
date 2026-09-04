package com.eligijus.deeper.presentation.login

import com.eligijus.deeper.domain.model.LoginResult
import com.eligijus.deeper.domain.repository.`interface`.AuthRepositoryInterface
import com.eligijus.deeper.domain.request.LoginRequestOutcome
import com.eligijus.deeper.domain.request.RequestError
import com.eligijus.deeper.domain.usecase.LoginUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is empty`() {
        val viewModel = createViewModel()

        val state = viewModel.uiState.value

        assertEquals("", state.email)
        assertEquals("", state.password)
        assertFalse(state.isLoading)
        assertNull(state.errorMessage)
    }

    @Test
    fun `email and password updates state`() {
        val viewModel = createViewModel()

        viewModel.onEmailChanged("test@example.com")
        viewModel.onPasswordChanged("password")

        val state = viewModel.uiState.value

        assertEquals("test@example.com", state.email)
        assertEquals("password", state.password)
        assertTrue(state.canLogin)
    }

    @Test
    fun `successful login produces success`() = runTest {
        val loginResult = LoginResult(
            token = "token",
            userId = 1L,
            scans = emptyList()
        )

        val repository = FakeAuthRepository(
            outcome = LoginRequestOutcome.Success(loginResult)
        )

        val viewModel = createViewModel(repository)

        viewModel.onEmailChanged("test@example.com")
        viewModel.onPasswordChanged("password")
        viewModel.login()

        advanceUntilIdle()

        val state = viewModel.uiState.value

        assertFalse(state.isLoading)
        assertNull(state.errorMessage)
    }

    @Test
    fun `failed login shows error`() = runTest {
        val repository = FakeAuthRepository(
            outcome = LoginRequestOutcome.Failure(
                RequestError.InvalidCredentials
            )
        )

        val viewModel = createViewModel(repository)

        viewModel.onEmailChanged("test@example.com")
        viewModel.onPasswordChanged("wrong")
        viewModel.login()

        advanceUntilIdle()

        val state = viewModel.uiState.value

        assertFalse(state.isLoading)
        assertEquals(
            "Incorrect email or password.",
            state.errorMessage
        )
    }

    private fun createViewModel(
        repository: AuthRepositoryInterface = FakeAuthRepository()
    ): LoginViewModel {
        return LoginViewModel(
            loginUseCase = LoginUseCase(repository)
        )
    }
}