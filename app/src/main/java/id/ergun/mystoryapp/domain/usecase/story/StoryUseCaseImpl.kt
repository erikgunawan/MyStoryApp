package id.ergun.mystoryapp.domain.usecase.story

import id.ergun.mystoryapp.common.util.ResponseWrapper
import id.ergun.mystoryapp.data.remote.model.StoryFormRequest
import id.ergun.mystoryapp.domain.model.BaseDomainModel
import id.ergun.mystoryapp.domain.repository.story.StoryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * @author erikgunawan
 * Created 27/09/22 at 22.37
 */
class StoryUseCaseImpl @Inject constructor(private val repository: StoryRepository): StoryUseCase {
    override suspend fun createStory(request: StoryFormRequest): Flow<ResponseWrapper<BaseDomainModel>> {
        return repository.createStory(request)
    }
}