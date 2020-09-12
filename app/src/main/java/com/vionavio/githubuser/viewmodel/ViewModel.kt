package com.vionavio.githubuser.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vionavio.githubuser.connection.Client
import com.vionavio.githubuser.model.User
import com.vionavio.githubuser.response.SearchResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ViewModel : ViewModel() {

    private val searchResultsLiveData: MutableLiveData<List<User>> = MutableLiveData()
    private val userDataLiveData: MutableLiveData<List<User>> = MutableLiveData()
    private val followingResultLive: MutableLiveData<List<User>> = MutableLiveData()
    private val followersResultLive: MutableLiveData<List<User>> = MutableLiveData()

    val searchResults: LiveData<List<User>> = searchResultsLiveData
    val userData: LiveData<List<User>> = userDataLiveData
    val following: LiveData<List<User>> = followingResultLive
    val followers: LiveData<List<User>> = followersResultLive


    fun searchUser(newText: String) {
        val call = Client.service.getSearch(newText)
        call.enqueue(object : Callback<SearchResponse> {
            override fun onResponse(
                call: Call<SearchResponse>,
                response: Response<SearchResponse>
            ) {
                if (response.isSuccessful) {
                    val listUser = response.body()?.items
                    searchResultsLiveData.postValue(listUser)
                }
            }

            override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                searchResultsLiveData.postValue(emptyList())
            }
        })
    }

    fun getUserApi() {
        val call = Client.service.getUsers()
        call.enqueue(object : Callback<SearchResponse> {

            override fun onResponse(
                call: Call<SearchResponse>,
                response: Response<SearchResponse>
            ) {
                val listUser = response.body()?.items
                userDataLiveData.postValue(listUser)
            }

            override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                userDataLiveData.postValue(emptyList())
            }
        })
    }

    fun getFollowing(userName: String) {
        val call: Call<List<User>> = Client.service.getFollowing(userName)
        call.enqueue(object : Callback<List<User>> {
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                if (response.isSuccessful) {
                    val list: List<User>? = response.body().orEmpty()
                    followingResultLive.postValue(list)
                }
            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                followingResultLive.postValue(emptyList())
            }
        })
    }

    fun getFollowers(userName: String) {
        val call: Call<List<User>> = Client.service.getFollowers(userName)
        call.enqueue(object : Callback<List<User>> {
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                if (response.isSuccessful) {
                    val list: List<User>? = response.body().orEmpty()
                    followersResultLive.postValue(list)
                }
            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                followersResultLive.postValue(emptyList())
            }
        })
    }
}