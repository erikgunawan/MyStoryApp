package id.ergun.mystoryapp.data.remote

import id.ergun.mystoryapp.data.remote.model.AuthRequest
import id.ergun.mystoryapp.data.remote.model.BaseResponse
import id.ergun.mystoryapp.data.remote.model.LoginResponse
import id.ergun.mystoryapp.data.remote.model.StoriesResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

/**
 * @author erikgunawan
 * Created 24/09/22 at 23.26
 */
interface ApiService {

    @POST("v1/register")
    suspend fun register(@Body request: AuthRequest): Response<LoginResponse>

    @POST("v1/login")
    suspend fun login(@Body request: AuthRequest): Response<LoginResponse>

    @Multipart
    @POST("v1/stories")
    suspend fun createStory(
        @HeaderMap headers: Map<String, String>,
        @Part photo:  MultipartBody.Part,
        @Part("description") description : RequestBody,
        @Part("lat") lat : RequestBody?,
        @Part("lon") long : RequestBody?
    ): Response<BaseResponse>

    @GET("v1/stories")
    suspend fun getStories(@HeaderMap headers: Map<String, String>, @QueryMap params: HashMap<String, String>): Response<StoriesResponse>
}