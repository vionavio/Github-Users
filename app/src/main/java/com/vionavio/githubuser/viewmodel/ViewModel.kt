package com.vionavio.githubuser.viewmodel

import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vionavio.githubuser.adapter.SectionAdapter
import com.vionavio.githubuser.response.SearchResponse
import com.vionavio.githubuser.connection.Client
import com.vionavio.githubuser.model.User
import com.vionavio.githubuser.view.ComponentFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ViewModel : ViewModel() {

    private val searchResultsLiveData: MutableLiveData<List<User>> = MutableLiveData()
    private val userDataLiveData: MutableLiveData<List<User>> = MutableLiveData()
    private val detailLiveData: MutableLiveData<List<User>> = MutableLiveData()
    val searchResults: LiveData<List<User>> = searchResultsLiveData
    val userData: LiveData<List<User>> = userDataLiveData
    val detailUserData: LiveData<List<User>> = userDataLiveData


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

    fun getUserApi() {
        val call = Client.service.getUsers()
        call.enqueue(object : Callback<SearchResponse> {

            override fun onResponse(call: Call<SearchResponse>, response: Response<SearchResponse>) {
                val listUser = response.body()?.items
                userDataLiveData.postValue(listUser)
            }

            override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                userDataLiveData.postValue(emptyList())
            }
        })
    }


    fun getFollowing(userName: String, title: String) {
        val call = Client.service.getFollowing(userName)
        call.enqueue(object : Callback<List<User>> {
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                if (response.isSuccessful) {

                    val list = ArrayList<User>(response.body().orEmpty())
                    detailLiveData.postValue(list)
//                    adapter.addFragment(
//                        ComponentFragment.newInstance(
//                            list
//                        ), title
//                    )
                }
            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
            }
        })
    }

//    fun getFollowers(userName: String, title: String) {
//        val call = Client.service.getFollowers(userName)
//        call.enqueue(object : Callback<List<User>> {
//            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
//                if (response.isSuccessful) {
//                    val listUser = ArrayList<User>(response.body().orEmpty())
//                    adapter.addFragment(
//                        ComponentFragment.newInstance(
//                            listUser
//                        ), title
//                    )
//                }
//            }
//
//            override fun onFailure(call: Call<List<User>>, t: Throwable) {
//            }
//        })
//    }
}