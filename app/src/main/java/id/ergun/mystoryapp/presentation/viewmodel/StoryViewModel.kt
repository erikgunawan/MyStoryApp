package id.ergun.mystoryapp.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.ergun.mystoryapp.common.util.ResponseWrapper
import id.ergun.mystoryapp.data.remote.model.StoryFormRequest
import id.ergun.mystoryapp.domain.model.BaseDomainModel
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

    private val _storyList = MutableLiveData<ResponseWrapper<ArrayList<StoryDataModel>>>()
    val stories = _storyList

    private val _createStoryResponse = MutableLiveData<ResponseWrapper<BaseDomainModel>>()
    val createStoryResponse = _createStoryResponse




    lateinit var selectedStory: StoryDataModel

    fun createStory(request: StoryFormRequest) {
        viewModelScope.launch {
//            val request = StoryFormRequest(description, File.createTempFile("abc","def"))

            storyUseCase.createStory(request).collect {
                _createStoryResponse.postValue(it)
            }
        }
    }
}