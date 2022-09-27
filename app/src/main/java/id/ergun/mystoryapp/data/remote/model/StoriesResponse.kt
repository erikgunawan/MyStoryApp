package id.ergun.mystoryapp.data.remote.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import id.ergun.mystoryapp.data.remote.model.Story.Companion.mapToDomainModel
import id.ergun.mystoryapp.domain.model.StoryDataModel

/**
 * @author erikgunawan
 * Created 24/09/22 at 22.46
 */
data class StoriesResponse(
    @Expose @SerializedName("error") val error: Boolean?,
    @Expose @SerializedName("message") val message: String?,
    @Expose @SerializedName("listStory") val listStory: ArrayList<Story>?
) {

    companion object {

        fun mapToDomainModelList(item: StoriesResponse): ArrayList<StoryDataModel> {
            val list = arrayListOf<StoryDataModel>()
            item.listStory?.forEach {
                list.add(mapToDomainModel(it))
            }
            return list
        }
    }
}