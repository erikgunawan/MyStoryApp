package id.ergun.mystoryapp.domain.repository.story

import id.ergun.mystoryapp.common.util.ResponseWrapper
import id.ergun.mystoryapp.data.remote.model.BaseResponse
import id.ergun.mystoryapp.data.remote.model.StoryFormRequest
import id.ergun.mystoryapp.domain.model.StoryDataModel
import kotlinx.coroutines.flow.Flow
import retrofit2.http.Body

/**
 * @author erikgunawan
 * Created 27/09/22 at 22.06
 */
interface StoryRepository {
    suspend fun createStory(@Body request: StoryFormRequest): Flow<ResponseWrapper<BaseResponse>>
    suspend fun createStoryGuest(@Body request: StoryFormRequest): Flow<ResponseWrapper<BaseResponse>>
    suspend fun getStories(): Flow<ResponseWrapper<ArrayList<StoryDataModel>>>
}