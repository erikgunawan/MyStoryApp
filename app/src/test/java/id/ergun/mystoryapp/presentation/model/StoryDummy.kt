package id.ergun.mystoryapp.presentation.model

import androidx.paging.PagingData
import id.ergun.mystoryapp.common.util.ResponseWrapper
import id.ergun.mystoryapp.data.remote.model.StoryFormRequest
import id.ergun.mystoryapp.domain.model.BaseDomainModel
import id.ergun.mystoryapp.domain.model.StoryDataModel
import kotlinx.coroutines.flow.flow
import java.io.File

/**
 * @author erikgunawan
 * Created 04/12/22 at 01.39
 */
object StoryDummy {

    fun getStoryParams(): HashMap<String, String>  {
        val params = hashMapOf<String, String>()
        params["location"] = "1"
        return params
    }

    fun generateDummyStories(): ArrayList<StoryDataModel> {
        val stories = ArrayList<StoryDataModel>()

        for (i in 0..10) {
            stories.add(
                StoryDataModel(
                    createdAt = "2022-12-03T17:59:51.330Z",
                    description = "Deskripsi $i",
                    id = "Story-$i",
                    lat = -2.234234234,
                    lon = 160.3423423,
                    photoUrl = "https://story-api.dicoding.dev/images/stories/photos-1670090391327_l0w--YjZ.jpg",
                    name = "Story $i"
                )
            )
        }
        return stories
    }

    fun getDummyStories() = flow {
        emit(ResponseWrapper.success(generateDummyStories()))
    }

    fun getPagingDummyStories() = flow {
        emit(PagingData.from(generateDummyStories()))
    }

    fun getDummyStoryCreateRequest(): StoryFormRequest {
        return StoryFormRequest(
            description = "Testing 12345",
            photo = File.createTempFile("story-abc", "jpg"),
            lat = -2.234234234,
            lon = 160.3423423
        )
    }

    fun getDummyStoryCreated() = flow {
        emit(ResponseWrapper.success(BaseDomainModel(
            error = false,
            message = "Story created"
        )))
    }
}