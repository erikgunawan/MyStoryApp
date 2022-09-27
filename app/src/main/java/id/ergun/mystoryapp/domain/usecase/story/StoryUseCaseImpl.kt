package id.ergun.mystoryapp.domain.usecase.story

import id.ergun.mystoryapp.common.util.ResponseWrapper
import id.ergun.mystoryapp.domain.model.StoryDataModel
import id.ergun.mystoryapp.domain.repository.story.StoryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * @author erikgunawan
 * Created 27/09/22 at 22.37
 */
class StoryUseCaseImpl @Inject constructor(private val repository: StoryRepository): StoryUseCase {
    override suspend fun getStories(): Flow<ResponseWrapper<ArrayList<StoryDataModel>>> {
        return repository.getStories()
    }
}