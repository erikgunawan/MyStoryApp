package id.ergun.mystoryapp.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import id.ergun.mystoryapp.common.util.ResponseWrapper
import id.ergun.mystoryapp.domain.usecase.auth.AuthUseCase
import id.ergun.mystoryapp.presentation.model.AuthDummy
import id.ergun.mystoryapp.util.MainDispatcherRule
import id.ergun.mystoryapp.util.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

/**
 * @author erikgunawan
 * Created 04/12/22 at 00.37
 */
@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class LoginViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var authUseCase: AuthUseCase

    private lateinit var loginViewModel: LoginViewModel

    @Before
    fun setup() {
        loginViewModel = LoginViewModel(authUseCase)
    }

    @Test
    fun `when Do Login Should Not Null And Return Success`() = runTest {
        val request = AuthDummy.getLoginRequest()

        Mockito.`when`(authUseCase.login(request))
            .thenReturn(AuthDummy.getDummyLogin())

        loginViewModel.login(request)
        val actualResponse = loginViewModel.loginResponse.getOrAwaitValue()

        Mockito.verify(authUseCase).login(request)

        Assert.assertNotNull(actualResponse.data)
        Assert.assertTrue(actualResponse.status == ResponseWrapper.Status.SUCCESS)
    }

    @Test
    fun `when Do Login Should Return Error`() = runTest {
        val request = AuthDummy.getLoginRequest()

        Mockito.`when`(authUseCase.login(request))
            .thenReturn(flow {
                emit(ResponseWrapper.error("Error"))
            })

        loginViewModel.login(request)
        val actualResponse = loginViewModel.loginResponse.getOrAwaitValue()

        Mockito.verify(authUseCase).login(request)

        Assert.assertTrue(actualResponse.status == ResponseWrapper.Status.ERROR)
    }
}