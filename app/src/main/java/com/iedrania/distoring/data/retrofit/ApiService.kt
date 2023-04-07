package com.iedrania.distoring.data.retrofit

import com.iedrania.distoring.data.model.LoginResponse
import com.iedrania.distoring.data.model.RegisterResponse
import com.iedrania.distoring.data.model.StoryResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
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