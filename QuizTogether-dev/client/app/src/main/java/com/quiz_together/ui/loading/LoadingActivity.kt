package com.quiz_together.ui.loading

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.facebook.FacebookSdk
import com.facebook.appevents.AppEventsLogger
import com.quiz_together.BasePresenter
import com.quiz_together.R
import com.quiz_together.data.Repository
import com.quiz_together.ui.base.LoadingFragment
import com.quiz_together.ui.base.LoadingPresenter
import com.quiz_together.util.replaceFragmentInActivity
import com.quiz_together.util.setupActionBar

class LoadingActivity : AppCompatActivity() {

    private fun initToolbar(){
        setupActionBar(R.id.toolbar) {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)

        initToolbar();

        val fragment = supportFragmentManager
                .findFragmentById(R.id.fl_content) as LoadingFragment? ?:
        LoadingFragment.newInstance().also {

            replaceFragmentInActivity(it, R.id.fl_content)
        }

        LoadingPresenter(Repository ,fragment)

        FacebookSdk.sdkInitialize(getApplicationContext())
        AppEventsLogger.activateApp(this);

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }





}
