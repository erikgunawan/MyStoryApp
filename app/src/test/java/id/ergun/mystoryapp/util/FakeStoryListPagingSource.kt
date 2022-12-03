package id.ergun.mystoryapp.util

import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.recyclerview.widget.ListUpdateCallback
import id.ergun.mystoryapp.domain.model.StoryDataModel
import kotlinx.coroutines.flow.Flow

/**
 * @author erikgunawan
 * Created 04/12/22 at 04.56
 */
class FakeStoryListPagingSource : PagingSource<Int, Flow<List<StoryDataModel>>>() {
    companion object {
        fun snapshot(items: List<StoryDataModel>): PagingData<StoryDataModel> {
            return PagingData.from(items)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Flow<List<StoryDataModel>>>): Int {
        return 0
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Flow<List<StoryDataModel>>> {
        return LoadResult.Page(emptyList(), 0, 1)
    }
}

val noopListUpdateCallback = object : ListUpdateCallback {
    override fun onInserted(position: Int, count: Int) {}
    override fun onRemoved(position: Int, count: Int) {}
    override fun onMoved(fromPosition: Int, toPosition: Int) {}
    override fun onChanged(position: Int, count: Int, payload: Any?) {}
}