package id.ergun.mystoryapp.data.repository.auth

import id.ergun.mystoryapp.common.util.ResponseWrapper
import id.ergun.mystoryapp.common.util.getResult
import id.ergun.mystoryapp.data.remote.ApiService
import id.ergun.mystoryapp.data.remote.model.Auth
import id.ergun.mystoryapp.data.remote.model.AuthRequest
import id.ergun.mystoryapp.domain.model.AuthDataModel
import id.ergun.mystoryapp.domain.repository.auth.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

/**
 * @author erikgunawan
 * Created 01/10/22 at 21.49
 */
class AuthRepositoryImpl @Inject constructor(private val apiService: ApiService): AuthRepository {

    override suspend fun register(request: AuthRequest): Flow<ResponseWrapper<AuthDataModel>> {
        return flow {
            try {
                emit(apiService.register(request).getResult {
                    Auth.mapToDomainModel(it.loginResult)
                })
            } catch (exception: Exception) {
                ResponseWrapper.error("exception", null)
            }
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun login(request: AuthRequest): Flow<ResponseWrapper<AuthDataModel>> {
        return flow {
            try {
                emit(apiService.login(request).getResult {
                    Auth.mapToDomainModel(it.loginResult)
                })
            } catch (exception: Exception) {
                ResponseWrapper.error("exception", null)
            }
        }.flowOn(Dispatchers.IO)
    }

}