package id.ergun.mystoryapp.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import id.ergun.mystoryapp.data.remote.ApiService
import id.ergun.mystoryapp.data.remote.model.StoriesResponse
import id.ergun.mystoryapp.domain.model.StoryDataModel

/**
 * @author erikgunawan
 * Created 02/10/22 at 01.03
 */
class StoryListDataSource(
 private val apiService: ApiService
) : PagingSource<Int, StoryDataModel>() {

 override suspend fun load(params: LoadParams<Int>): LoadResult<Int, StoryDataModel> {

  val parameter = HashMap<String, String>()
  val position = params.key ?: 1
  parameter["page"] = position.toString()
  return try {
   val response = apiService.getStories(getHeaderMap(), parameter)
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


 private fun getHeaderMap(): Map<String, String> {
  val headerMap = mutableMapOf<String, String>()
  headerMap["Authorization"] = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLXBvVE51NVJOZkhfZ19nYXEiLCJpYXQiOjE2NjQ2MzcwNTF9.edYWxCyVK2M1G6SZSudt_EMEmgKmgLUm67zVp3MPqds"
  return headerMap
 }

 private fun toLoadResult(
  data: List<StoryDataModel>,
  prevKey: Int? = null,
  nextKey: Int? = null
 ): LoadResult<Int, StoryDataModel> {
  return LoadResult.Page(
   data = data,
   prevKey = prevKey,
   nextKey = nextKey
  )
 }
}
