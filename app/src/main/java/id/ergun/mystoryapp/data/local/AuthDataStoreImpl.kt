package id.ergun.mystoryapp.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import id.ergun.mystoryapp.common.util.Const.PREFERENCES_KEY_TOKEN
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

/**
 * @author erikgunawan
 * Created 16/11/22 at 00.06
 */

class AuthDataStoreImpl @Inject constructor(private val authDataStore: DataStore<Preferences>) :
    AuthDataStore {
    override suspend fun setToken(token: String) {
        Result.runCatching {
            authDataStore.edit { preferences ->
                preferences[PREFERENCES_KEY_TOKEN] = token
            }
        }
    }

    override suspend fun getToken(): Result<String> {
        return Result.runCatching {
            val flow = authDataStore.data.catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }.map { preferences ->
                preferences[PREFERENCES_KEY_TOKEN]
            }
            val value = flow.firstOrNull() ?: ""
            value
        }
    }
}