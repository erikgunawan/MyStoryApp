package id.ergun.mystoryapp.data

import okhttp3.Interceptor
import okhttp3.Response

/**
 * @author erikgunawan
 * Created 24/09/22 at 23.38
 */

class AppInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request().newBuilder()

        return chain.proceed(builder.build())
    }
}