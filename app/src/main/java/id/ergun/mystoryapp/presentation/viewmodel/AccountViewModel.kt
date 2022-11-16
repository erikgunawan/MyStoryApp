package id.ergun.mystoryapp.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.ergun.mystoryapp.common.util.ResponseWrapper
import id.ergun.mystoryapp.domain.usecase.auth.AuthUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author erikgunawan
 * Created 16/11/22 at 23.22
 */
@HiltViewModel
class AccountViewModel @Inject constructor(
    private val authUseCase: AuthUseCase
) : ViewModel() {

    private val _token = MutableLiveData<ResponseWrapper<String>>()
    val token = _token

    private val _logoutResponse = MutableLiveData<ResponseWrapper<String>>()
    val logoutResponse = _logoutResponse


    fun getToken() {
        viewModelScope.launch {
            authUseCase.getToken().collect {
                _token.postValue(it)
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            authUseCase.logout().collect {
                _logoutResponse.postValue(it)
            }
        }
    }

}