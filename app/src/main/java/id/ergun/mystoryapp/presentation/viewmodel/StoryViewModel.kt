package id.ergun.mystoryapp.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.ergun.mystoryapp.common.util.ResponseWrapper
import id.ergun.mystoryapp.domain.model.StoryDataModel
import id.ergun.mystoryapp.domain.usecase.story.StoryUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author erikgunawan
 * Created 27/09/22 at 22.39
 */
@HiltViewModel
class StoryViewModel @Inject constructor(
    private val storyUseCase: StoryUseCase
) : ViewModel() {


    private val _movieList = MutableLiveData<ResponseWrapper<ArrayList<StoryDataModel>>>()
    val stories = _movieList

    init {
        fetchMovies()
    }

    private fun fetchMovies() {
        viewModelScope.launch {
            storyUseCase.getStories().collect {
                _movieList.value = it
            }
        }
    }

}