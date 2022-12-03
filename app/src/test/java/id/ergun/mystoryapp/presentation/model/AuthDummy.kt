package id.ergun.mystoryapp.presentation.model

import id.ergun.mystoryapp.common.util.ResponseWrapper
import id.ergun.mystoryapp.data.remote.model.AuthRequest
import id.ergun.mystoryapp.domain.model.AuthDataModel
import id.ergun.mystoryapp.domain.model.BaseDomainModel
import kotlinx.coroutines.flow.flow

/**
 * @author erikgunawan
 * Created 04/12/22 at 03.24
 */
object AuthDummy {

    fun getRegisterRequest(): AuthRequest {
        return AuthRequest(
            name = "Testing",
            email = "testing12345@gmail.com",
            password = "testing123"
        )
    }

    private fun generateDummyRegister(): BaseDomainModel {
        return BaseDomainModel(
            error = false,
            message = "Register success"
        )
    }

    fun getDummyRegister() = flow {
        emit(ResponseWrapper.success(generateDummyRegister()))
    }

    fun getLoginRequest(): AuthRequest {
        return AuthRequest(
            name = null,
            email = "testing12345@gmail.com",
            password = "testing123"
        )
    }

    private fun generateDummyLogin(): AuthDataModel {
        return AuthDataModel(
            userId = "user-poTNu5RNfH_g_gaq",
            name = "tess",
            token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLXBvVE51NVJOZkhfZ19nYXEiLCJpYXQiOjE2NzAxMDEzMjZ9.lGHqe9jnLTUW2kEjFAHKkvCyvzQPaiNxSNO-iY3iz7s"
        )
    }

    fun getDummyLogin() = flow {
        emit(ResponseWrapper.success(generateDummyLogin()))
    }

    fun getDummyToken() = flow {
        emit(ResponseWrapper.success("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLXBvVE51NVJOZkhfZ19nYXEiLCJpYXQiOjE2NzAxMDEzMjZ9.lGHqe9jnLTUW2kEjFAHKkvCyvzQPaiNxSNO-iY3iz7s"))
    }


    fun getDummyLogout() = flow {
        emit(ResponseWrapper.success("Berhasil logout"))
    }
}