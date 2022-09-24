package id.ergun.mystoryapp.common.util

/**
 * @author erikgunawan
 * Created 24/09/22 at 23.43
 */
class ResponseWrapper<out T>(val status: Status, val data: T?, val message: String?, val code: Int? = null) {

    enum class Status {
        SUCCESS,
        ERROR,
        LOADING,
        EMPTY_DATA
    }

    companion object {
        fun <T> success(data: T): ResponseWrapper<T> {
            return ResponseWrapper(Status.SUCCESS, data, null)
        }

        fun <T> success(data: T, message: String): ResponseWrapper<T> {
            return ResponseWrapper(Status.SUCCESS, data, message)
        }

        fun <T> emptyData(message: String, data: T? = null): ResponseWrapper<T> {
            return ResponseWrapper(Status.EMPTY_DATA, data, message)
        }

        fun <T> error(message: String, data: T? = null, code: Int? = null): ResponseWrapper<T> {
            return ResponseWrapper(Status.ERROR, data, message, code)
        }

        fun <T> loading(data: T? = null): ResponseWrapper<T> {
            return ResponseWrapper(Status.LOADING, data, null)
        }
    }
}