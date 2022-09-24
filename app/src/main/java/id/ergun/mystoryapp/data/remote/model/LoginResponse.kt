package id.ergun.mystoryapp.data.remote.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author erikgunawan
 * Created 24/09/22 at 22.53
 */
data class LoginResponse(
    @Expose @SerializedName("error") val error: Boolean?,
    @Expose @SerializedName("message") val message: String?,
    @Expose @SerializedName("loginResult") val loginResult: Auth
)