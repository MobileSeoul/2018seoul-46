package com.quiz_together.ui.setting

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.quiz_together.R
import com.quiz_together.data.Repository
import com.quiz_together.ui.login.LoginFragment
import com.quiz_together.ui.login.LoginPresenter
import com.quiz_together.util.replaceFragmentInActivity
import com.quiz_together.util.setupActionBar

class SettingActivity: AppCompatActivity() {

    private fun initToolbar(){
        setupActionBar(R.id.toolbar) {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        initToolbar();
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }





}
