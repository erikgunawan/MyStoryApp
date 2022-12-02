package id.ergun.mystoryapp.data.remote.model


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import id.ergun.mystoryapp.domain.model.StoryDataModel

data class Story(
    @Expose @SerializedName("createdAt") val createdAt: String?,
    @Expose @SerializedName("description") val description: String?,
    @Expose @SerializedName("id") val id: String?,
    @Expose @SerializedName("lat") val lat: Double?,
    @Expose @SerializedName("lon") val lon: Double?,
    @Expose @SerializedName("name") val name: String?,
    @Expose @SerializedName("photoUrl") val photoUrl: String?
) {
    companion object {

        fun mapToDomainModel(item: Story): StoryDataModel {
            return StoryDataModel(
                item.createdAt ?: "",
                item.description ?: "",
                item.id ?: "",
                item.lat,
                item.lon,
                item.name,
                item.photoUrl
            )
        }
    }
}