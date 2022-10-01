package id.ergun.mystoryapp.domain.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import id.ergun.mystoryapp.domain.usecase.auth.AuthUseCase
import id.ergun.mystoryapp.domain.usecase.auth.AuthUseCaseImpl
import id.ergun.mystoryapp.domain.usecase.story.StoryUseCase
import id.ergun.mystoryapp.domain.usecase.story.StoryUseCaseImpl
import id.ergun.mystoryapp.domain.usecase.story.paging.StoryListUseCase
import id.ergun.mystoryapp.domain.usecase.story.paging.StoryListUseCaseImpl

/**
 * @author erikgunawan
 * Created 27/09/22 at 22.53
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class UseCaseModule {
 @Binds
 abstract fun bindAuthUseCase(impl: AuthUseCaseImpl): AuthUseCase
 @Binds
 abstract fun bindStoryUseCase(impl: StoryUseCaseImpl): StoryUseCase
 @Binds
 abstract fun bindStoryListUseCase(impl: StoryListUseCaseImpl): StoryListUseCase
}