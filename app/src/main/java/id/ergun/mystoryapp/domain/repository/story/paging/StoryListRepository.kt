package id.ergun.mystoryapp.domain.repository.story.paging

import androidx.paging.PagingData
import id.ergun.mystoryapp.domain.model.StoryDataModel
import kotlinx.coroutines.flow.Flow

/**
 * @author erikgunawan
 * Created 02/10/22 at 01.16
 */
interface StoryListRepository {
    fun getStories(): Flow<PagingData<StoryDataModel>>
}