package id.ergun.mystoryapp.domain.usecase.story.paging

import androidx.paging.PagingData
import id.ergun.mystoryapp.domain.model.StoryDataModel
import id.ergun.mystoryapp.domain.repository.story.paging.StoryListRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * @author erikgunawan
 * Created 02/10/22 at 01.19
 */
class StoryListUseCaseImpl @Inject constructor(private val repository: StoryListRepository): StoryListUseCase {
    override fun getStories(
        token: String,
        scope: CoroutineScope
    ): Flow<PagingData<StoryDataModel>> {
        return repository.getStories(token, scope)
    }
}