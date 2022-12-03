package id.ergun.mystoryapp.domain.usecase.story.paging

import androidx.paging.PagingData
import id.ergun.mystoryapp.domain.model.StoryDataModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

/**
 * @author erikgunawan
 * Created 02/10/22 at 01.18
 */
interface StoryListUseCase {
    fun getStories(scope: CoroutineScope): Flow<PagingData<StoryDataModel>>
}