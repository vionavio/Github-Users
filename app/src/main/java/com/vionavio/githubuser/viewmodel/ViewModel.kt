package com.vionavio.githubuser.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vionavio.githubuser.response.SearchResponse
import com.vionavio.githubuser.connection.Client
import com.vionavio.githubuser.model.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ViewModel: ViewModel() {

    private val searchResultsLiveData: MutableLiveData<List<User>> = MutableLiveData()
    val searchResults: LiveData<List<User>> = searchResultsLiveData

    fun searchUser(newText: String) {
        val call = Client.service.getSearch(newText)
        call.enqueue(object : Callback<SearchResponse> {
            override fun onResponse(call: Call<SearchResponse>, response: Response<SearchResponse>) {
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
}