package com.vionavio.githubuser.view

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import android.database.sqlite.SQLiteConstraintException
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.vionavio.githubuser.R
import com.vionavio.githubuser.adapter.SectionAdapter
import com.vionavio.githubuser.connection.Client
import com.vionavio.githubuser.db.DatabaseContract.UserColumns.Companion.AVATAR
import com.vionavio.githubuser.db.DatabaseContract.UserColumns.Companion.USERNAME
import com.vionavio.githubuser.db.UserHelper
import com.vionavio.githubuser.model.User
import com.vionavio.githubuser.util.glide.GlideApp
import kotlinx.android.synthetic.main.activity_detail.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class DetailActivity : AppCompatActivity() {

    private lateinit var adapter: SectionAdapter
    private lateinit var userHelper: UserHelper
    private var isFavorite: Boolean = false
    private lateinit var menuItem: Menu
    private lateinit var userName : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        initView()
        tabs.setupWithViewPager(view_pager)
    }

    private fun initView() {

        supportActionBar?.title = getString(R.string.detail_user)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        userName = getIntentData()
        getDetailUser(userName)

        adapter =
            SectionAdapter(supportFragmentManager)
        view_pager.adapter = adapter

        userHelper = UserHelper.getInstance(applicationContext)
        userHelper.open()
        favoriteState()
    }

    private fun favoriteState() {
        userName = getIntentData()
        val result = userHelper.queryByLogin(userName)
        val favorite = (1 .. result.count).map {
            result.apply {
                moveToNext()
                getInt(result.getColumnIndexOrThrow(USERNAME))
            }
        }
        if (favorite.isNotEmpty()) isFavorite = true
    }

    private fun getIntentData(): String {
        val user = intent.getParcelableExtra<User>(EXTRA_USER)
        return user?.username ?: ""
    }

    private fun getFollowing(userName: String, title: String) {
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

    private fun getFollowers(userName: String, title: String) {
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

    private fun getDetailUser(userName: String) {
        val call = Client.service.getDetailUser(userName)
        call.enqueue(object : Callback<User> {
            override fun onFailure(call: Call<User>, t: Throwable) {
            }

            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    val user = response.body()
                    initDetailUser(user)

                    getFollowing(userName, " ${getString(R.string.following)}")
                    getFollowers(userName, " ${getString(R.string.followers)}")
                }
            }
        })
    }

    @SuppressLint("SetTextI18n")
    private fun initDetailUser(user: User?) {
        tv_name.text = user?.name
        tv_location.text = user?.location
        if (user?.location == null) ico_location.visibility = View.GONE
        tv_company.text = user?.company
        if (user?.company == null) ico_company.visibility = View.GONE
        number_repository.text = user?.repository.toString()
        number_followers.text = user?.followers.toString()
        number_following.text = user?.following.toString()


        GlideApp.with(this@DetailActivity)
            .load(user?.avatar)
            .circleCrop()
            .placeholder(android.R.color.darker_gray)
            .error(android.R.color.darker_gray)
            .into(img_detail)
    }

    companion object {
        const val EXTRA_USER = "user"
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        if (menu != null) {
            menuItem = menu
        }
        setFavorite()
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        val share = menu.findItem(R.id.action_share)
        share.isVisible = true
        val favourite = menu.findItem(R.id.action_favourite)
        favourite.isVisible = true
        val alarm = menu.findItem(R.id.action_alarm)
        alarm.isVisible = true
        val language = menu.findItem(R.id.action_language)
        language.isVisible = true
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_share -> {
                shareUser()
                true
            }
            R.id.action_favourite -> {
                val user = intent.getParcelableExtra<User>(EXTRA_USER)
                if (isFavorite) removeFavoriteUser() else addFavoriteUser(user)

                isFavorite = !isFavorite
                setFavorite()
                true
            }
            R.id.action_alarm -> {
                startActivity(Intent(this@DetailActivity, SettingPreferenceActivity::class.java))
                true
            }
            R.id.action_language -> {
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setFavorite() {

        if (isFavorite) {
            menuItem.getItem(1)?.icon = ContextCompat.getDrawable(this, R.drawable.favorite)
        } else menuItem.getItem(1)?.icon = ContextCompat.getDrawable(this, R.drawable.ic_favorite)
    }

    private fun shareUser() {
        val user = intent.getParcelableExtra<User>(EXTRA_USER)

        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(
                Intent.EXTRA_TEXT,
                "${getString(R.string.check_profile)} *${tv_name.text}* ${getString(R.string.on)} Github github.com/${user?.username} "
            )
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
    }

    private fun removeFavoriteUser() {
        userName = getIntentData()
        try {
            val result = userHelper.deleteByUsername(userName)
            val text = resources.getString(R.string.delete)
            Toast.makeText(this, text, Toast.LENGTH_SHORT).show()

            Log.d("on:Remove..", result.toString())
        } catch (e: SQLiteConstraintException) {
            e.printStackTrace()
        }
    }

    private fun addFavoriteUser(user: User?) {
        if (user != null) {
            val values = ContentValues()
            values.put(USERNAME, user.username)
            values.put(AVATAR, user.avatar)
            val result = userHelper.insert(values)
            showResult(result)
        }
    }

    private fun showResult(result: Long) {
        when {
            result > 0 -> {
                Toast.makeText(this, "Berhasil menambah data", Toast.LENGTH_SHORT).show()
            }
            else -> {
                Toast.makeText(this, "Gagal menambah data", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        super.onBackPressed()
        return true
    }
}