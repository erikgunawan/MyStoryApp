package id.ergun.mystoryapp.domain.usecase.auth

import id.ergun.mystoryapp.common.util.ResponseWrapper
import id.ergun.mystoryapp.data.remote.model.AuthRequest
import id.ergun.mystoryapp.domain.model.AuthDataModel
import id.ergun.mystoryapp.domain.repository.auth.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * @author erikgunawan
 * Created 01/10/22 at 21.52
 */
class AuthUseCaseImpl @Inject constructor(private val repository: AuthRepository): AuthUseCase {
    override suspend fun register(request: AuthRequest): Flow<ResponseWrapper<AuthDataModel>> {
        return repository.register(request)
    }

    override suspend fun login(request: AuthRequest): Flow<ResponseWrapper<AuthDataModel>> {
        return repository.login(request)
    }
}