package id.ergun.mystoryapp.domain.model

/**
 * @author erikgunawan
 * Created 27/09/22 at 22.24
 */
data class StoryDataModel(
    val createdAt: String,
    val description: String?,
    val id: String?,
    val lat: Double?,
    val lon: Double?,
    val name: String?,
    val photoUrl: String?
)