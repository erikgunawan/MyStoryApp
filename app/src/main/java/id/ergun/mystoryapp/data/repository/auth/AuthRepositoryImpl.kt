package id.ergun.mystoryapp.data.repository.auth

import id.ergun.mystoryapp.common.util.ResponseWrapper
import id.ergun.mystoryapp.common.util.getResult
import id.ergun.mystoryapp.data.local.AuthDataStore
import id.ergun.mystoryapp.data.remote.ApiService
import id.ergun.mystoryapp.data.remote.model.Auth
import id.ergun.mystoryapp.data.remote.model.AuthRequest
import id.ergun.mystoryapp.domain.model.AuthDataModel
import id.ergun.mystoryapp.domain.model.BaseDomainModel
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
class AuthRepositoryImpl @Inject constructor(private val apiService: ApiService,
                                             private val authDataStore: AuthDataStore): AuthRepository {

    override suspend fun register(request: AuthRequest): Flow<ResponseWrapper<BaseDomainModel>> {
        return flow {
            try {
                val response = apiService.register(request).getResult {
                    BaseDomainModel(
                        it.error ?: false,
                        it.message ?: ""
                    )
                }
                emit(response)
            } catch (exception: Exception) {
                emit(ResponseWrapper.error(exception.message.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun login(request: AuthRequest): Flow<ResponseWrapper<AuthDataModel>> {
        return flow {
            try {
                val response = apiService.login(request).getResult {
                    Auth.mapToDomainModel(it.loginResult)
                }
                emit(response)

                if (response.status == ResponseWrapper.Status.SUCCESS) {
                    authDataStore.setToken(response.data?.token ?: "")
                }
            } catch (exception: Exception) {
                emit(ResponseWrapper.error(exception.message.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun getToken(): Flow<ResponseWrapper<String>> {
        return flow {
            val token = authDataStore.getToken().getOrDefault("")

            if (token.isNotEmpty()) {
                emit(ResponseWrapper.success(token))
            } else {
                emit(ResponseWrapper.error("Unauthenticated"))
            }
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun logout(): Flow<ResponseWrapper<String>> {
        return flow {
            authDataStore.setToken("")
            emit(ResponseWrapper.success("Berhasil logout"))
        }.flowOn(Dispatchers.IO)
    }
}