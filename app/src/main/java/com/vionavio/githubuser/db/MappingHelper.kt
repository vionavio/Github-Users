package com.vionavio.githubuser.db

import android.database.Cursor
import com.vionavio.githubuser.model.User

object MappingHelper {

    fun mapCursorToArrayList(favoriteUserCursor: Cursor?): MutableList<User> {
        val favoriteUserItemsList = mutableListOf<User>()

        favoriteUserCursor?.apply {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(DatabaseContract.UserColumns._ID))
                val username = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.USERNAME))
                val avatar = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.AVATAR))
                favoriteUserItemsList.add(User(id, username, avatar, null, null, null, null, null, null))
            }
        }
        return favoriteUserItemsList
    }
}