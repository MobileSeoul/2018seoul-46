package com.quiz_together.ui.event

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.quiz_together.R
import com.quiz_together.data.Repository
import com.quiz_together.util.replaceFragmentInActivity
import com.quiz_together.util.setupActionBar


class EventActivity : AppCompatActivity() {

    private fun initToolbar(){
        setupActionBar(R.id.toolbar) {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event)

        initToolbar();

        val id = intent.getStringExtra(EXTRA_ID)

        val eventFragment = supportFragmentManager
                .findFragmentById(R.id.fl_content) as EventFragment? ?:
        EventFragment.newInstance(id).also {

            replaceFragmentInActivity(it, R.id.fl_content)
        }

        EventPresenter(id,Repository ,eventFragment)

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    companion object {
        const val EXTRA_ID = "ID"
    }




}
