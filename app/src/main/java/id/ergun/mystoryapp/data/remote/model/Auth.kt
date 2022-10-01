package id.ergun.mystoryapp.data.remote.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import id.ergun.mystoryapp.domain.model.AuthDataModel

/**
 * @author erikgunawan
 * Created 24/09/22 at 22.52
 */
data class Auth(
    @Expose @SerializedName("userId") val userId: String?,
    @Expose @SerializedName("name") val name: String?,
    @Expose @SerializedName("token") val token: String?
) {

    companion object {
        fun mapToDomainModel(item: Auth): AuthDataModel {
            return AuthDataModel(
                item.userId,
                item.name,
                item.token
            )
        }
    }
}