package id.ergun.mystoryapp.domain.repository.story

import id.ergun.mystoryapp.common.util.ResponseWrapper
import id.ergun.mystoryapp.data.remote.model.BaseResponse
import id.ergun.mystoryapp.data.remote.model.StoryFormRequest
import id.ergun.mystoryapp.domain.model.StoryDataModel
import kotlinx.coroutines.flow.Flow

/**
 * @author erikgunawan
 * Created 27/09/22 at 22.06
 */
interface StoryRepository {
    suspend fun createStory(request: StoryFormRequest): Flow<ResponseWrapper<BaseResponse>>
    suspend fun createStoryGuest(request: StoryFormRequest): Flow<ResponseWrapper<BaseResponse>>
    suspend fun getStories(): Flow<ResponseWrapper<ArrayList<StoryDataModel>>>
}