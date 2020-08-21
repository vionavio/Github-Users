package com.vionavio.githubuser.view

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.vionavio.githubuser.R
import com.vionavio.githubuser.adapter.SectionAdapter
import com.vionavio.githubuser.connection.Client
import com.vionavio.githubuser.model.User
import com.vionavio.githubuser.util.GlideApp
import kotlinx.android.synthetic.main.activity_detail.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class DetailActivity : AppCompatActivity() {

    private lateinit var adapter: SectionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        initView()
        tabs.setupWithViewPager(view_pager)
    }

    private fun initView() {
        setSupportActionBar(detail_toolbar)
        supportActionBar?.title = getString(R.string.detail_user)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        detail_toolbar.setNavigationOnClickListener {
            startActivity(
                Intent(
                    applicationContext,
                    MainActivity::class.java
                )
            )
        }
        val userName = getIntentData()
        getDetailUser(userName)

        adapter =
            SectionAdapter(supportFragmentManager)
        view_pager.adapter = adapter
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
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_share -> {
                shareUser()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
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
}