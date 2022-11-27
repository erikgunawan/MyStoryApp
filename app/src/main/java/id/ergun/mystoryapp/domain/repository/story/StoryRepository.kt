package id.ergun.mystoryapp.domain.repository.story

import id.ergun.mystoryapp.common.util.ResponseWrapper
import id.ergun.mystoryapp.data.remote.model.StoryFormRequest
import id.ergun.mystoryapp.domain.model.BaseDomainModel
import id.ergun.mystoryapp.domain.model.StoryDataModel
import kotlinx.coroutines.flow.Flow

/**
 * @author erikgunawan
 * Created 27/09/22 at 22.06
 */
interface StoryRepository {
    suspend fun createStory(request: StoryFormRequest): Flow<ResponseWrapper<BaseDomainModel>>
    suspend fun getStories(params: HashMap<String, String>): Flow<ResponseWrapper<ArrayList<StoryDataModel>>>
}