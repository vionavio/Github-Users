package com.vionavio.githubuser.viewmodel

import com.vionavio.githubuser.R
import com.vionavio.githubuser.connection.Client
import com.vionavio.githubuser.model.User
import androidx.lifecycle.ViewModel
import com.vionavio.githubuser.adapter.SectionAdapter
import com.vionavio.githubuser.view.ComponentFragment
import com.vionavio.githubuser.view.DetailActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ViewModelDetail: ViewModel() {
    private lateinit var adapter: SectionAdapter

    fun getFollowing(userName: String, title: String) {
        val call = Client.service.getFollowing(userName)
        call.enqueue(object : Callback<List<User>> {
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                if (response.isSuccessful) {
                    val list = ArrayList<User>(response.body().orEmpty())
                    adapter.addFragment(
                        ComponentFragment.newInstance(
                            list
                        ), title
                    )
                }
            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
            }
        })
    }

    fun getFollowers(userName: String, title: String) {
        val call = Client.service.getFollowers(userName)
        call.enqueue(object : Callback<List<User>> {
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                if (response.isSuccessful) {
                    val listUser = ArrayList<User>(response.body().orEmpty())
                    adapter.addFragment(
                        ComponentFragment.newInstance(
                            listUser
                        ), title
                    )
                }
            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
            }
        })
    }
}