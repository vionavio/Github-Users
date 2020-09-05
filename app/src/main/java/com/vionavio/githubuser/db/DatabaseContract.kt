package com.vionavio.githubuser.db

import android.provider.BaseColumns

class DatabaseContract {
    internal class UserColumns: BaseColumns{
        companion object {
            const val TABLE_NAME = "github_user"
            const val _ID = "_id"
            const val USERNAME = "username"
            const val AVATAR = "avatar"
            //tambah simpen data lain
        }
    }
}