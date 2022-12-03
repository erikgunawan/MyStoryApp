package id.ergun.mystoryapp.data.repository.story.paging

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import id.ergun.mystoryapp.data.local.AuthDataStore
import id.ergun.mystoryapp.data.paging.StoryListDataSource
import id.ergun.mystoryapp.data.remote.ApiService
import id.ergun.mystoryapp.domain.model.StoryDataModel
import id.ergun.mystoryapp.domain.repository.story.paging.StoryListRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * @author erikgunawan
 * Created 02/10/22 at 01.14
 */

class StoryListRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val authDataStore: AuthDataStore
) : StoryListRepository {

    override fun getStories(
        scope: CoroutineScope
    ): Flow<PagingData<StoryDataModel>> {
        return Pager(config = PagingConfig(10)) {
            StoryListDataSource(apiService, authDataStore)
        }.flow.cachedIn(scope)
    }
}