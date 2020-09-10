package com.vionavio.githubuser.connection

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Client {

    companion object {
        private val retrofit: Retrofit by lazy {
            Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://api.github.com/")
                .build()
        }

        val service by lazy {
            val create: Service = retrofit.create(
                Service::class.java)
            create
        }
    }
}
