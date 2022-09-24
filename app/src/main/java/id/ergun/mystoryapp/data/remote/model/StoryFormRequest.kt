package id.ergun.mystoryapp.data.remote.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author erikgunawan
 * Created 24/09/22 at 23.32
 */
data class StoryFormRequest(
    @Expose @SerializedName("description") val description: String?,
    @Expose @SerializedName("photo") val photo: String?,
    @Expose @SerializedName("lat") val lat: Double?,
    @Expose @SerializedName("lon") val lon: Double?,
)
//description as string
//photo as file, must be a valid image file, max size 1MB
//lat as float, optional
//lon as float, optional
