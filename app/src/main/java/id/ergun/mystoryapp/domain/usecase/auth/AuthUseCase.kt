package id.ergun.mystoryapp.domain.usecase.auth

import id.ergun.mystoryapp.common.util.ResponseWrapper
import id.ergun.mystoryapp.data.remote.model.AuthRequest
import id.ergun.mystoryapp.domain.model.AuthDataModel
import kotlinx.coroutines.flow.Flow

/**
 * @author erikgunawan
 * Created 01/10/22 at 21.52
 */
interface AuthUseCase {
    suspend fun register(request: AuthRequest): Flow<ResponseWrapper<AuthDataModel>>
    suspend fun login(request: AuthRequest): Flow<ResponseWrapper<AuthDataModel>>
}