package id.ergun.mystoryapp.data.remote

import id.ergun.mystoryapp.data.remote.model.*
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HeaderMap
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
    suspend fun createStory(@Body request: StoryFormRequest): Response<BaseResponse>

    @POST("v1/stories/guest")
    suspend fun createStoryGuest(@Body request: StoryFormRequest): Response<BaseResponse>

    @GET("v1/stories")
    suspend fun getStories(@HeaderMap headers: Map<String, String>): Response<StoriesResponse>
}