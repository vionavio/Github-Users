package com.vionavio.consumerapp.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User (
    @field:SerializedName("id")
    var id: Int?,

    @SerializedName("login")
    val username: String? = null,
    @SerializedName("avatar_url")
    val avatar: String? = null,
    @SerializedName("followers")
    val followers: Int? = 0,
    @SerializedName("following")
    val following: Int? = 0,
    val name: String? = null,
    val location: String? = null,
    val company: String? = null,
    @SerializedName("public_repos")
    val repository: Int? = 0
) : Parcelable



