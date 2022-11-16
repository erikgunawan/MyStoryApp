package id.ergun.mystoryapp.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import id.ergun.mystoryapp.common.util.Helper.getHeaderMap
import id.ergun.mystoryapp.data.local.AuthDataStore
import id.ergun.mystoryapp.data.remote.ApiService
import id.ergun.mystoryapp.data.remote.model.StoriesResponse
import id.ergun.mystoryapp.domain.model.StoryDataModel

/**
 * @author erikgunawan
 * Created 02/10/22 at 01.03
 */
class StoryListDataSource(
 private val apiService: ApiService,
 private val authDataStore: AuthDataStore
) : PagingSource<Int, StoryDataModel>() {

 override suspend fun load(params: LoadParams<Int>): LoadResult<Int, StoryDataModel> {

  val parameter = HashMap<String, String>()
  val position = params.key ?: 1
  parameter["page"] = position.toString()
  return try {
   val token = authDataStore.getToken().getOrDefault("")
    val response = apiService.getStories(getHeaderMap(token), parameter)
    val data = response.body()?.let { StoriesResponse.mapToDomainModelList(it) }
    val stories = arrayListOf<StoryDataModel>().apply {
     if (data != null) {
      addAll(data)
     }
    }
    toLoadResult(
     data = stories,
     // activate below function if you want to load on paging backward
     //prevKey = if (position == 1) null else position - 1,
     nextKey = if (stories.isEmpty()) null else position + 1
    )
  } catch (e: Exception) {
   e.printStackTrace()
   LoadResult.Error(e)
  }
 }

 override fun getRefreshKey(state: PagingState<Int, StoryDataModel>): Int? {
  return null
 }

 private fun toLoadResult(
  data: List<StoryDataModel>,
  prevKey: Int? = null,
  nextKey: Int? = null
 ): PagingSource.LoadResult<Int, StoryDataModel> {
  return PagingSource.LoadResult.Page(
   data = data,
   prevKey = prevKey,
   nextKey = nextKey
  )
 }
}
