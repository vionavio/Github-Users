package com.vionavio.githubuser.view

import android.content.Intent
import android.database.ContentObserver
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.vionavio.githubuser.R
import com.vionavio.githubuser.adapter.FavoriteAdapter
import com.vionavio.githubuser.db.DatabaseContract.UserColumns.Companion.CONTENT_URI
import com.vionavio.githubuser.db.MappingHelper
import com.vionavio.githubuser.db.UserHelper
import com.vionavio.githubuser.model.User
import kotlinx.android.synthetic.main.activity_favorite.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FavoriteActivity : AppCompatActivity() {

    private lateinit var helper: UserHelper
    private lateinit var adapter: FavoriteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)
        supportActionBar?.title = getString(R.string.favourite)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        helper = UserHelper.getInstance(applicationContext)
        helper.open()

        initView()

        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)

        val myObserver = object : ContentObserver(handler) {
            override fun onChange(selfChange: Boolean) {
                loadUsersAsync()
            }
        }
        contentResolver.registerContentObserver(CONTENT_URI, true, myObserver)
    }

    private fun initView() {

        adapter = FavoriteAdapter(onClick = { user: User ->
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra(DetailActivity.EXTRA_USER, user)
            startActivity(intent)
        }, onLongClick = { user, position ->
            deleteUser(user, position)
        })
        rv_favorite.layoutManager = LinearLayoutManager(this)
        rv_favorite.adapter = adapter

    }

    private fun deleteUser(user: User, position: Int) {
        val result = helper.deleteByUsername(username = user.username.toString())
        if (result > 0) {
            adapter.removeItem(position)
            Toast.makeText(this, R.string.success_delete, Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(this, R.string.failed_delete, Toast.LENGTH_LONG).show()
        }
    }

    private fun loadUsersAsync() {
        GlobalScope.launch(Dispatchers.Main) {
            progress_bar.visibility = View.VISIBLE
            val deferredNotes = async(Dispatchers.IO) {
                val cursor = helper.queryAll()
                MappingHelper.mapCursorToArrayList(cursor)
            }
            progress_bar.visibility = View.INVISIBLE
            val notes: MutableList<User> = deferredNotes.await()
            addUsersToAdapter(notes)
        }
    }

    private fun addUsersToAdapter(notes: MutableList<User>) {
        when {
            notes.isNotEmpty() -> {
                adapter.addAll(notes)
            }
            else -> {
                adapter.addAll(emptyList())
                Toast.makeText(this@FavoriteActivity, R.string.no_data, Toast.LENGTH_LONG)
                    .show()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        val alarm = menu.findItem(R.id.action_alarm)
        alarm.isVisible = true
        val language = menu.findItem(R.id.action_language)
        language.isVisible = true
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_alarm -> {
                startActivity(Intent(this@FavoriteActivity, SettingPreferenceActivity::class.java))
                true
            }
            R.id.action_language -> {
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onResume() {
        super.onResume()
        initView()
        loadUsersAsync()
    }

    override fun onSupportNavigateUp(): Boolean {
        super.onBackPressed()
        return true
    }
}
