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
 * Created 04/12/22 at 00.38
 */
@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class RegisterViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var authUseCase: AuthUseCase

    private lateinit var registerViewModel: RegisterViewModel

    @Before
    fun setup() {
        registerViewModel = RegisterViewModel(authUseCase)
    }

    @Test
    fun `when Do Register Should Return Success`() = runTest {
        val request = AuthDummy.getRegisterRequest()

        Mockito.`when`(authUseCase.register(request))
            .thenReturn(AuthDummy.getDummyRegister())

        registerViewModel.register(request)
        val actualResponse = registerViewModel.registerResponse.getOrAwaitValue()

        Mockito.verify(authUseCase).register(request)

        Assert.assertTrue(actualResponse.status == ResponseWrapper.Status.SUCCESS)
    }

    @Test
    fun `when Do Register Should Return Error`() = runTest {
        val request = AuthDummy.getRegisterRequest()

        Mockito.`when`(authUseCase.register(request))
            .thenReturn(flow {
                emit(ResponseWrapper.error("Error"))
            })

        registerViewModel.register(request)
        val actualResponse = registerViewModel.registerResponse.getOrAwaitValue()

        Mockito.verify(authUseCase).register(request)

        Assert.assertTrue(actualResponse.status == ResponseWrapper.Status.ERROR)
    }
}