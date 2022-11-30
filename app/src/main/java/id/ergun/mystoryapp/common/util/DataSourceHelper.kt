package id.ergun.mystoryapp.common.util

import retrofit2.Response
import timber.log.Timber

/**
 * @author erikgunawan
 * Created 27/09/22 at 22.21
 */
fun <T, R> Response<T>.getResult(transform: (T) -> (R)): ResponseWrapper<R> {
    try {
        val response = this
        if (response.isSuccessful && response.body() != null) {
            val body = transform(response.body()!!)
            if (body != null) {
                return success(body)
            }
        }
        return error("Terjadi kesalahan")
    } catch (e: Exception) {
        return error(e.message ?: e.toString())
    }
}


private fun <T> success(body: T): ResponseWrapper<T> {
    return ResponseWrapper.success(body)
}

private fun <T> error(message: String, code: Int? = null, data: T? = null): ResponseWrapper<T> {
    Timber.e(message)
    return ResponseWrapper.error(message, data = data, code = code)
}