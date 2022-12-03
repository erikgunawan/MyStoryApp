package id.ergun.mystoryapp.common.util

import com.google.gson.Gson
import id.ergun.mystoryapp.data.remote.model.BaseResponse
import okhttp3.ResponseBody
import retrofit2.Response
import timber.log.Timber
import java.io.IOException

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
        } else {
            Timber.e("catch")
            val errorResponse = response.errorBody()?.parseErrorBody()
            Timber.e(Gson().toJson(errorResponse))
            return error(errorResponse?.message ?: "", code())
        }

        return error(message = "", code = 999)
    } catch (e: Exception) {
        return error(e.message ?: e.toString())
    }
}


fun ResponseBody.parseErrorBody(): BaseResponse? {
    val gson = Gson()
    val adapter = gson.getAdapter(BaseResponse::class.java)
    return try {
        adapter.fromJson(string())
    } catch (e: IOException) {
        e.printStackTrace()
        null
    }
}

private fun <T> success(body: T): ResponseWrapper<T> {
    return ResponseWrapper.success(body)
}

private fun <T> error(message: String, code: Int? = null, data: T? = null): ResponseWrapper<T> {
    Timber.e(message)
    return ResponseWrapper.error(message, data = data, code = code)
}