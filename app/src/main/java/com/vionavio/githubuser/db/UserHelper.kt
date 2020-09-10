package com.vionavio.githubuser.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import com.vionavio.githubuser.db.DatabaseContract.UserColumns.Companion.TABLE_NAME
import com.vionavio.githubuser.db.DatabaseContract.UserColumns.Companion.USERNAME
import com.vionavio.githubuser.db.DatabaseContract.UserColumns.Companion._ID

class UserHelper(context: Context?) {

    companion object {
        private const val DATABASE_TABLE = TABLE_NAME
        private lateinit var databaseHelper: DatabaseHelper
        private lateinit var database: SQLiteDatabase

        private var INSTANCE: UserHelper? = null
        fun getInstance(context: Context): UserHelper =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: UserHelper(context)
            }
    }

    init {
        databaseHelper = DatabaseHelper(context)
    }

    @Throws(SQLException::class)
    fun open() {
        database = databaseHelper.writableDatabase
    }

    fun queryAll(): Cursor {
        return database.query(
            DATABASE_TABLE,
            null,
            null,
            null,
            null,
            null,
            "$_ID ASC")
    }

    fun insert(values: ContentValues?): Long {
        return database.insert(DATABASE_TABLE, null, values)
    }

    fun deleteByUsername(username: String) : Int {
        return database.delete(DATABASE_TABLE, "$USERNAME = '$username'", null)
    }

    fun queryByUsername(login : String) : Cursor {
        return database.query(
            DATABASE_TABLE, null, "$USERNAME = ?", arrayOf(login), null, null, null, null
        )
    }
}