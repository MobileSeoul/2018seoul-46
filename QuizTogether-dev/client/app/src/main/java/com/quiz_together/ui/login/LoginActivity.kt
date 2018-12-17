package com.quiz_together.ui.login

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.quiz_together.R
import com.quiz_together.data.Repository
import com.quiz_together.util.replaceFragmentInActivity
import com.quiz_together.util.setupActionBar

class LoginActivity : AppCompatActivity() {

    private fun initToolbar(){
        setupActionBar(R.id.toolbar) {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        initToolbar();

        val loginFragment = supportFragmentManager
                .findFragmentById(R.id.fl_content) as LoginFragment? ?:
        LoginFragment.newInstance().also {
            replaceFragmentInActivity(it, R.id.fl_content)
        }

        LoginPresenter(Repository ,loginFragment)

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }






}
