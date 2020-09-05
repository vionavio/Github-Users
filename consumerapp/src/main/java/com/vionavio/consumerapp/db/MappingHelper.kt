package com.vionavio.consumerapp.db

import android.database.Cursor
import com.vionavio.consumerapp.model.User
import java.util.ArrayList

object MappingHelper {

    fun mapCursorToArrayList(favoriteUserCursor: Cursor?): ArrayList<User> {
        val favoriteUserItemsList = ArrayList<User>()

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