package com.quiz_together.ui.create

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.quiz_together.R
import com.quiz_together.data.Repository
import com.quiz_together.util.replaceFragmentInActivity
import com.quiz_together.util.setupActionBar

class CreateActivity : AppCompatActivity() {

    private fun initToolbar() {
        setupActionBar(R.id.toolbar) {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_back)
            setDisplayShowHomeEnabled(true)
            setDisplayShowTitleEnabled(false)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create)

        initToolbar()

        val fragment = supportFragmentManager.findFragmentById(R.id.fl_content) as CreateFragment?
                ?: CreateFragment.newInstance().also {
                    replaceFragmentInActivity(it, R.id.fl_content)
                }

        CreatePresenter(Repository, fragment)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
