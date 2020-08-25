package com.vionavio.githubuser.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.vionavio.githubuser.R
import kotlinx.android.synthetic.main.activity_setting_preference.*

class SettingPreferenceActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting_preference)
        initView()

        supportFragmentManager.beginTransaction()
            .add(R.id.setting_preference, PreferenceFragment())
            .commit()
    }

    private fun initView(){
        setSupportActionBar(detail_toolbar)
        supportActionBar?.title = getString(R.string.reminder_title)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        super.onBackPressed()
        return true
    }
}