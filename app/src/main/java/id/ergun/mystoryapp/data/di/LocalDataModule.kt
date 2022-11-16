package id.ergun.mystoryapp.data.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import id.ergun.mystoryapp.data.local.AuthDataStore
import id.ergun.mystoryapp.data.local.AuthDataStoreImpl
import javax.inject.Singleton

/**
 * @author erikgunawan
 * Created 16/11/22 at 00.07
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class LocalDataModule {
    @Binds
    @Singleton
    abstract fun bindAuthDataStorePreferencesRepository(dataStorePreferencesRepositoryImpl: AuthDataStoreImpl): AuthDataStore

    companion object {
        @Provides
        @Singleton
        fun provideAuthDataStorePreferences(@ApplicationContext applicationContext: Context): DataStore<Preferences> {
            return applicationContext.authDataStore
        }
    }

}

val Context.authDataStore: DataStore<Preferences> by preferencesDataStore(
    name = "authDataStore"
)