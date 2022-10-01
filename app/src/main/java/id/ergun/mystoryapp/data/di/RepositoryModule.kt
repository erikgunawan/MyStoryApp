package id.ergun.mystoryapp.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import id.ergun.mystoryapp.data.repository.auth.AuthRepositoryImpl
import id.ergun.mystoryapp.data.repository.story.StoryRepositoryImpl
import id.ergun.mystoryapp.domain.repository.auth.AuthRepository
import id.ergun.mystoryapp.domain.repository.story.StoryRepository

/**
 * @author erikgunawan
 * Created 27/09/22 at 22.52
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository
    @Binds
    abstract fun bindStoryRepository(impl: StoryRepositoryImpl): StoryRepository
}