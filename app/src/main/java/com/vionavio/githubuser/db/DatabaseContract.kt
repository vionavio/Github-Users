package com.vionavio.githubuser.db

import android.net.Uri
import android.provider.BaseColumns

object DatabaseContract {
    const val AUTHORITY = "com.vionavio.githubuser"
    const val SCHEME = "content"

    internal class UserColumns: BaseColumns{
        companion object {
            const val TABLE_NAME = "github_user"
            const val _ID = "_id"
            const val USERNAME = "username"
            const val AVATAR = "avatar"

            var CONTENT_URI : Uri = Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_NAME)
                .build()
        }
    }
}