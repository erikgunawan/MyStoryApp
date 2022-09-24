package id.ergun.mystoryapp.data.remote.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author erikgunawan
 * Created 24/09/22 at 23.30
 */
data class AuthRequest(
    @Expose @SerializedName("name") val name: String?,
    @Expose @SerializedName("email") val email: String?,
    @Expose @SerializedName("password") val password: String?,
)