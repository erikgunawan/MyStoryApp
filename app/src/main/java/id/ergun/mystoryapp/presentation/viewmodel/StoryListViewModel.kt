package id.ergun.mystoryapp.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import dagger.hilt.android.lifecycle.HiltViewModel
import id.ergun.mystoryapp.common.util.ResponseWrapper
import id.ergun.mystoryapp.domain.model.StoryDataModel
import id.ergun.mystoryapp.domain.usecase.auth.AuthUseCase
import id.ergun.mystoryapp.domain.usecase.story.paging.StoryListUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author erikgunawan
 * Created 02/10/22 at 01.21
 */
@HiltViewModel
class StoryListViewModel @Inject constructor(
    private val storyListUseCase: StoryListUseCase,
    private val authUseCase: AuthUseCase
) : ViewModel() {

    private val _storyList = MutableLiveData<PagingData<StoryDataModel>>()
    val storyList = _storyList

    fun getStories(): Flow<PagingData<StoryDataModel>> {
        return storyListUseCase.getStories("", viewModelScope)
    }

    fun getStores() {
        viewModelScope.launch {
            authUseCase.getToken().collect {
                when (it.status) {
                    ResponseWrapper.Status.SUCCESS -> {
                        if (it.data.isNullOrEmpty()) {
                            return@collect
                        }

                        storyListUseCase.getStories(it.data, viewModelScope).collectLatest { pagingData ->
                            _storyList.postValue(pagingData)
                        }
                    }
                    else -> {

                    }
                }
            }
        }
    }
}