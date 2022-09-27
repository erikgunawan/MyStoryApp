package id.ergun.mystoryapp.data.repository.story

import id.ergun.mystoryapp.common.util.ResponseWrapper
import id.ergun.mystoryapp.common.util.getResult
import id.ergun.mystoryapp.data.remote.ApiService
import id.ergun.mystoryapp.data.remote.model.BaseResponse
import id.ergun.mystoryapp.data.remote.model.StoriesResponse
import id.ergun.mystoryapp.data.remote.model.StoryFormRequest
import id.ergun.mystoryapp.domain.model.StoryDataModel
import id.ergun.mystoryapp.domain.repository.story.StoryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

/**
 * @author erikgunawan
 * Created 27/09/22 at 22.06
 */
class StoryRepositoryImpl @Inject constructor(private val apiService: ApiService): StoryRepository {

    override suspend fun createStory(request: StoryFormRequest): Flow<ResponseWrapper<BaseResponse>> {
        TODO("Not yet implemented")
    }

    override suspend fun createStoryGuest(request: StoryFormRequest): Flow<ResponseWrapper<BaseResponse>> {
        TODO("Not yet implemented")
    }

    override suspend fun getStories(): Flow<ResponseWrapper<ArrayList<StoryDataModel>>> {
        return flow {
            try {
                emit(apiService.getStories().getResult {
                    StoriesResponse.mapToDomainModelList(it)
                })
            } catch (exception: Exception) {
                ResponseWrapper.error("exception", null)
            }
        }.flowOn(Dispatchers.IO)
    }
}