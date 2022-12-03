package id.ergun.mystoryapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import id.ergun.mystoryapp.domain.model.StoryDataModel
import id.ergun.mystoryapp.domain.usecase.story.paging.StoryListUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * @author erikgunawan
 * Created 02/10/22 at 01.21
 */
@HiltViewModel
class StoryListViewModel @Inject constructor(
    private val storyListUseCase: StoryListUseCase
) : ViewModel() {

    fun getStories(): Flow<PagingData<StoryDataModel>> {
        return storyListUseCase.getStories().cachedIn(viewModelScope)
    }
}