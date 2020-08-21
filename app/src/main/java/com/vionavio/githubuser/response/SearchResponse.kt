package com.vionavio.githubuser.response

import com.google.gson.annotations.SerializedName
import com.vionavio.githubuser.model.User

data class SearchResponse (
    @SerializedName("items") val items: List<User> = mutableListOf()
)
