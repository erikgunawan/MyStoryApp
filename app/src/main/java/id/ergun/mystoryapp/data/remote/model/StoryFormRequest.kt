package id.ergun.mystoryapp.data.remote.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.File

/**
 * @author erikgunawan
 * Created 24/09/22 at 23.32
 */
data class StoryFormRequest(
    @Expose @SerializedName("description") val description: String,
    @Expose @SerializedName("photo") val photo: File,
    @Expose @SerializedName("lat") val lat: Double? = null,
    @Expose @SerializedName("lon") val lon: Double? = null
)