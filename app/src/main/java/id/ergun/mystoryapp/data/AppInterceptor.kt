package id.ergun.mystoryapp.data

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response

/**
 * @author erikgunawan
 * Created 24/09/22 at 23.38
 */

class AppInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request().newBuilder()

        val response = chain.proceed(builder.build())

//        if (response.code == 401) {
//            throw UnauthorizedException()
//            cacheStorage.setLogout()
//        }

        return response
    }

    companion object {
        const val ACCEPT = "Accept"
        const val AUTHORIZATION = "Authorization"
        const val FCM_TOKEN = "fcm_token"
    }
}