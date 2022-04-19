package com.dickyarya.storyapp.api

import com.dickyarya.storyapp.model.LoginResponse
import com.dickyarya.storyapp.model.PostResponse
import com.dickyarya.storyapp.model.RegisterResponse
import com.dickyarya.storyapp.model.StoryResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface ApiConfig {
   @FormUrlEncoded
   @POST("login")
   fun login(
      @Field("email") email: String,
      @Field("password") password: String
   ): Call<LoginResponse>

   @FormUrlEncoded
   @POST("register")
   fun register(
      @Field("name") name: String,
      @Field("email") email: String,
      @Field("password") password: String
   ): Call<RegisterResponse>

   @GET("stories")
   fun getStories(
      @Header("Authorization") auth: String
   ): Call<StoryResponse>

   @Multipart
   @POST("stories")
   fun postStory(
      @Header("Authorization") auth: String,
      @Part file: MultipartBody.Part,
      @Part("description") description: String
   ): Call<PostResponse>
}