package com.vionavio.githubuser.connection

import com.vionavio.githubuser.response.SearchResponse
import com.vionavio.githubuser.model.User
import retrofit2.Call
import retrofit2.http.*

interface Service {

    @GET("search/users")
    @Headers("Authorization: token 82f75d46d16f37a3ac1f28ec3a76ea6b9823ea90")
    fun getSearch(@Query("q") q: String): Call<SearchResponse>

    @GET("users/{username}")
    fun getDetailUser(@Path("username") username: String): Call<User>

    @GET("users/{username}/followers")
    fun getFollowers(@Path("username") username: String): Call<List<User>>

    @GET("users/{username}/following")
    fun getFollowing(@Path("username") username: String): Call<List<User>>
}