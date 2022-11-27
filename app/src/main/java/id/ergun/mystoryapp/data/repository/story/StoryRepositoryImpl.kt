package id.ergun.mystoryapp.data.repository.story

import id.ergun.mystoryapp.common.util.Const
import id.ergun.mystoryapp.common.util.Helper
import id.ergun.mystoryapp.common.util.ResponseWrapper
import id.ergun.mystoryapp.common.util.getResult
import id.ergun.mystoryapp.data.local.AuthDataStore
import id.ergun.mystoryapp.data.remote.ApiService
import id.ergun.mystoryapp.data.remote.model.StoriesResponse
import id.ergun.mystoryapp.data.remote.model.StoryFormRequest
import id.ergun.mystoryapp.domain.model.BaseDomainModel
import id.ergun.mystoryapp.domain.model.StoryDataModel
import id.ergun.mystoryapp.domain.repository.story.StoryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

/**
 * @author erikgunawan
 * Created 27/09/22 at 22.06
 */
class StoryRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val authDataStore: AuthDataStore
): StoryRepository {

    override suspend fun createStory(request: StoryFormRequest): Flow<ResponseWrapper<BaseDomainModel>> {
        return flow {
            try {
                val token = authDataStore.getToken().getOrDefault("")

                val description: RequestBody =
                    request.description.toRequestBody(Const.MEDIA_TYPE_TEXT_PLAIN.toMediaTypeOrNull())

                val profilePictureRequestBody =
                    request.photo.asRequestBody("image/*".toMediaTypeOrNull())

                val photoMultipartBody = MultipartBody.Part.createFormData(
                    "photo",
                    "photo_${System.currentTimeMillis()}.jpg",
                    profilePictureRequestBody
                )
                val response = apiService.createStory(
                    Helper.getHeaderMap(token),
                    photoMultipartBody,
                    description
                ).getResult {
                    BaseDomainModel(
                        it.error ?: false,
                        it.message ?: ""
                    )
                }
                emit(response)
            } catch (exception: Exception) {
                emit(ResponseWrapper.error("Terjadi kesalahan"))
            }
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun getStories(params: HashMap<String, String>): Flow<ResponseWrapper<ArrayList<StoryDataModel>>> {
        return flow {
            try {
                val token = authDataStore.getToken().getOrDefault("")

                val response = apiService.getStories(
                    Helper.getHeaderMap(token),
                    params
                ).getResult {
                    StoriesResponse.mapToDomainModelList(it)
                }
                emit(response)
            } catch (exception: Exception) {
                emit(ResponseWrapper.error("Terjadi kesalahan"))
            }
        }.flowOn(Dispatchers.IO)
    }
}