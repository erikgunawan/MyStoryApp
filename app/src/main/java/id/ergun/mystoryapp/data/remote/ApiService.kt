package id.ergun.mystoryapp.data.remote

import id.ergun.mystoryapp.data.remote.model.AuthRequest
import id.ergun.mystoryapp.data.remote.model.LoginResponse
import id.ergun.mystoryapp.data.remote.model.StoryFormRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * @author erikgunawan
 * Created 24/09/22 at 23.26
 */
interface ApiService {

    @POST("v1/register")
    suspend fun register(@Body request: AuthRequest): Response<LoginResponse>

    @POST("v1/login")
    suspend fun login(@Body request: AuthRequest): Response<LoginResponse>

    @POST("v1/stories")
    suspend fun createStory(@Body request: StoryFormRequest): Response<LoginResponse>

    @POST("v1/stories/guest")
    suspend fun createStoryGuest(@Body request: StoryFormRequest): Response<LoginResponse>

    @POST("v1/stories")
    suspend fun getStories(): Response<LoginResponse>
}