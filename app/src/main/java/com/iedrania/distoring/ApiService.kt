package com.iedrania.distoring

import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @Headers("Authorization: Bearer <token>")
    @GET("stories")
    fun getStories(): Call<StoryResponse>

    @FormUrlEncoded
    @POST("register")
    fun postRegister(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<RegisterResponse>

    @FormUrlEncoded
    @POST("login")
    fun postLogin(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginResponse>
}