package id.ergun.mystoryapp.domain.usecase.story

import id.ergun.mystoryapp.common.util.ResponseWrapper
import id.ergun.mystoryapp.domain.model.StoryDataModel
import kotlinx.coroutines.flow.Flow

/**
 * @author erikgunawan
 * Created 27/09/22 at 22.37
 */
interface StoryUseCase {
    suspend fun getStories(): Flow<ResponseWrapper<ArrayList<StoryDataModel>>>
}