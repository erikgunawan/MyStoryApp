package id.ergun.mystoryapp.domain.repository.auth

import id.ergun.mystoryapp.common.util.ResponseWrapper
import id.ergun.mystoryapp.data.remote.model.AuthRequest
import id.ergun.mystoryapp.domain.model.AuthDataModel
import kotlinx.coroutines.flow.Flow

/**
 * @author erikgunawan
 * Created 01/10/22 at 21.45
 */
interface AuthRepository {
    suspend fun register(request: AuthRequest): Flow<ResponseWrapper<AuthDataModel>>
    suspend fun login(request: AuthRequest): Flow<ResponseWrapper<AuthDataModel>>
}