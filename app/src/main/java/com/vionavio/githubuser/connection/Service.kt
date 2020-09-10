package com.vionavio.githubuser.connection

import com.vionavio.githubuser.model.User
import com.vionavio.githubuser.response.SearchResponse
import retrofit2.Call
import retrofit2.http.*


interface Service {

    @GET("users")
    fun getUsers() : Call<SearchResponse>

    @GET("search/users")
    fun getSearch(@Query("q") q: String): Call<SearchResponse>

    @GET("users/{username}")
    fun getDetailUser(@Path("username") username: String): Call<User>

    @GET("users/{username}/followers")
    fun getFollowers(@Path("username") username: String): Call<List<User>>

    @GET("users/{username}/following")
    fun getFollowing(@Path("username") username: String): Call<List<User>>
}