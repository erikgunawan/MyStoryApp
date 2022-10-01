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
 * Created 01/10/22 at 21.56
 */
@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authUseCase: AuthUseCase
) : ViewModel() {

    private val _registerResponse = MutableLiveData<ResponseWrapper<AuthDataModel>>()
    val registerResponse = _registerResponse

    fun register(request: AuthRequest) {
        viewModelScope.launch {
            authUseCase.register(request).collect {
                registerResponse.postValue(it)
            }
        }
    }

}