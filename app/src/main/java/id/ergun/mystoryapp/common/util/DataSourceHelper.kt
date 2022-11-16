package id.ergun.mystoryapp.common.util

import com.google.gson.Gson
import id.ergun.mystoryapp.data.remote.model.LoginResponse
import okhttp3.ResponseBody
import retrofit2.Response
import timber.log.Timber
import java.io.IOException

/**
 * @author erikgunawan
 * Created 27/09/22 at 22.21
 */

//fun <T, R> Response<T>.getResult(transform: (T) -> (R), throwable: (TaskErrorResponse?) -> ResponseWrapper<R>): ResponseWrapper<R> {
// try {
//  val response = this
//  if (response.isSuccessful && response.body() != null) {
//   val body = transform(response.body()!!)
//   if (body != null) {
//    return success(body)
//   }
//  }
//  return when {
//   response.code() == 422 -> {
//    val response = response.errorBody()!!.parseErrorBody()
//    error(response?.message.toString())
//   }
//   response.code() == 401 -> {
//    error("Unauthenticated. Silahkan melakukan login ulang.", code = 401)
//   }
//   response.code() == 400 -> {
//    val response = response.errorBody()!!.parseTaskErrorBody()
//    throwable.invoke(response)
//   }
//   else -> error("Terjadi kesalahan")
//  }
// } catch (e: Exception) {
//  return error(e.message ?: e.toString())
// }
//}

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

fun ResponseBody.parseErrorBody(): LoginResponse? {
 val gson = Gson()
 val adapter = gson.getAdapter(LoginResponse::class.java)
 try {
  return adapter.fromJson(string())
 } catch (e: IOException) {
  e.printStackTrace()
 }
 return null
}

//fun ResponseBody.parseTaskErrorBody(): TaskErrorResponse? {
// val gson = Gson()
// val adapter = gson.getAdapter(TaskErrorResponse::class.java)
// try {
//  return adapter.fromJson(string())
// } catch (e: IOException) {
//  e.printStackTrace()
// }
// return null
//}

private fun <T> success(body: T): ResponseWrapper<T> {
 return ResponseWrapper.success(body)
}

private fun <T> error(message: String, code: Int? = null, data: T? = null): ResponseWrapper<T> {
 Timber.e(message)
 return ResponseWrapper.error(message, data = data, code = code)
}