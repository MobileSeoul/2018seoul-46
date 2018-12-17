package com.quiz_together.ui.base

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.quiz_together.R
import com.quiz_together.data.Repository
import com.quiz_together.util.replaceFragmentInActivity
import com.quiz_together.util.setupActionBar

class BaseActivity : AppCompatActivity() {

    private fun initToolbar(){
        setupActionBar(R.id.toolbar) {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)

        initToolbar();

        val fragment = supportFragmentManager
                .findFragmentById(R.id.fl_content) as BaseFragment? ?:
        BaseFragment.newInstance().also {

            replaceFragmentInActivity(it, R.id.fl_content)
        }

        BasePresenter(Repository ,fragment)

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }





}
