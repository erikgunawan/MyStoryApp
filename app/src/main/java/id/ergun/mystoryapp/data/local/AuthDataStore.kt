package id.ergun.mystoryapp.data.local

/**
 * @author erikgunawan
 * Created 16/11/22 at 00.11
 */
interface AuthDataStore {
    suspend fun setToken(token: String)
    suspend fun getToken(): Result<String>
}