package id.ergun.mystoryapp.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.ergun.mystoryapp.common.util.ResponseWrapper
import id.ergun.mystoryapp.data.remote.model.AuthRequest
import id.ergun.mystoryapp.domain.model.AuthDataModel
import id.ergun.mystoryapp.domain.usecase.auth.AuthUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author erikgunawan
 * Created 01/10/22 at 21.55
 */
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authUseCase: AuthUseCase
) : ViewModel() {

    private val _loginResponse = MutableLiveData<ResponseWrapper<AuthDataModel>>()
    val loginResponse = _loginResponse

    fun login(request: AuthRequest) {
        viewModelScope.launch {
            authUseCase.login(request).collect {
                _loginResponse.postValue(it)
            }
        }
    }
}