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
 * Created 04/12/22 at 00.14
 */
@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class AccountViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var authUseCase: AuthUseCase

    private lateinit var accountViewModel: AccountViewModel

    @Before
    fun setup() {
        accountViewModel = AccountViewModel(authUseCase)
    }

    @Test
    fun `when Get Token Should Not Null Or Empty And Return Success`() = runTest {
        Mockito.`when`(authUseCase.getToken())
            .thenReturn(AuthDummy.getDummyToken())

        accountViewModel.getToken()
        val actualResponse = accountViewModel.token.getOrAwaitValue()

        Mockito.verify(authUseCase).getToken()

        Assert.assertTrue(!actualResponse.data.isNullOrEmpty())
        Assert.assertTrue(actualResponse.status == ResponseWrapper.Status.SUCCESS)
    }

    @Test
    fun `when Get Token Should Empty And Return Error`() = runTest {
        Mockito.`when`(authUseCase.getToken())
            .thenReturn(flow {
                emit(ResponseWrapper.error("Error"))
            })

        accountViewModel.getToken()
        val actualResponse = accountViewModel.token.getOrAwaitValue()

        Mockito.verify(authUseCase).getToken()

        Assert.assertTrue(actualResponse.status == ResponseWrapper.Status.ERROR)
    }

    @Test
    fun `when Do Logout Should Return Success`() = runTest {
        Mockito.`when`(authUseCase.logout())
            .thenReturn(AuthDummy.getDummyLogout())

        accountViewModel.logout()
        val actualResponse = accountViewModel.logoutResponse.getOrAwaitValue()

        Mockito.verify(authUseCase).logout()

        Assert.assertTrue(actualResponse.status == ResponseWrapper.Status.SUCCESS)
    }
}