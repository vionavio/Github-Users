package com.vionavio.githubuser.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.vionavio.githubuser.R
import com.vionavio.githubuser.adapter.ComponentAdapter
import com.vionavio.githubuser.model.User
import com.vionavio.githubuser.viewmodel.ViewModel
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), TextView.OnEditorActionListener {

    private lateinit var adapter: ComponentAdapter
    private lateinit var viewModel: ViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initData()
        initViewConfigure()
        et_search.setOnEditorActionListener(this)
    }

    private fun initData() {
        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            ViewModel::class.java
        )
        viewModel.getUserApi()
        viewModel.userData.observe(this, Observer { listUser: List<User>? ->
            false.showProgressBar()
            adapter.addAll(listUser)
        })

        viewModel.searchResults.observe(this, Observer { listUser: List<User>? ->
            false.showProgressBar()
            adapter.addAll(listUser)
        })
    }

    private fun initViewConfigure() {

        adapter = ComponentAdapter()
        rv_user_search.layoutManager = LinearLayoutManager(this)
        rv_user_search.itemAnimator = DefaultItemAnimator()
        rv_user_search.adapter = adapter
    }

    private fun Boolean.showProgressBar() {
        progress_bar.visibility = if (this) {
            VISIBLE
        } else {
            GONE
        }
    }

    override fun onEditorAction(p0: TextView?, p1: Int, p2: KeyEvent?): Boolean {
        if (p1 == EditorInfo.IME_ACTION_SEARCH) {
            val newText = p0?.text.toString()
            viewModel.searchUser(newText)
            true.showProgressBar()
            hideSoftKeyboard(this)
            return true
        }
        return false
    }

    private fun hideSoftKeyboard(activity: Activity) {
        val inputMethodManager =
            activity.getSystemService(
                Activity.INPUT_METHOD_SERVICE
            ) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(
            activity.currentFocus?.windowToken, 0
        )
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
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
            R.id.action_favourite -> {
                startActivity(Intent(this, FavoriteActivity::class.java))
                true
            }
            R.id.action_alarm -> {
                startActivity(Intent(this@MainActivity, SettingPreferenceActivity::class.java))
                true
            }
            R.id.action_language -> {
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
