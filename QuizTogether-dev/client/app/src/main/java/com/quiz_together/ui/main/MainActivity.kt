package com.quiz_together.ui.main

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Toast
import com.quiz_together.R
import com.quiz_together.ui.create.CreateActivity
import com.quiz_together.ui.main.home.HomeFragment
import com.quiz_together.ui.main.profile.ProfileFragment
import com.quiz_together.ui.quizing.QuizingActivity
import com.quiz_together.util.SC
import com.quiz_together.util.replace
import com.quiz_together.util.setupActionBar
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity#$#"
    private val FINISH_INTERVAL_TIME: Long = 2000
    private var backPressedTime: Long = 0

    private val homeFragment: HomeFragment by lazy {
        HomeFragment()
    }
    private val profileFragment: ProfileFragment by lazy {
        ProfileFragment()
    }

    private fun initToolbar(){
        setupActionBar(R.id.toolbar) {
            setDisplayShowHomeEnabled(true)
            setTitle("퀴즈홈")
        }
    }

    private fun initListener(){

        ibFindLock.setOnClickListener {

            //TODO 비공개방 넘버입력 후 들어갈 수 있게

//            val intent = Intent(applicationContext, CreateActivity::class.java)
//            startActivity(intent)
        }

        ibSetting.setOnClickListener {
            Log.i(TAG,"ibSetting.setOnClickListener")
        }

        ibCreate.setOnClickListener {
                val intent = Intent(applicationContext,CreateActivity::class.java)
                startActivity(intent)
        }

        ibPrf.setOnClickListener {
            ibSetting.visibility = View.VISIBLE
            ibFindLock.visibility = View.GONE

            ibPrf.setImageResource(R.drawable.icc_prf_on)
            ibHome.setImageResource(R.drawable.icc_home_off)

            replace(R.id.fl_content, profileFragment)
        }

        ibHome.setOnClickListener {
            ibSetting.visibility = View.GONE
            ibFindLock.visibility = View.VISIBLE

            ibHome.setImageResource(R.drawable.icc_home_on)
            ibPrf.setImageResource(R.drawable.icc_prf_off)

            replace(R.id.fl_content, homeFragment)

        }




    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        replace(R.id.fl_content, homeFragment)
        initToolbar()
        initListener()
//        bnv.disableShiftMode()
//        bnv.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)




    }

    override fun onResume() {
        super.onResume()

        // get broadcast from firebase for start broadcast
        var broadcastId = intent.getStringExtra(BROADCAST_ID)
        var userId = intent.getStringExtra(USER_ID)

        broadcastId?.run {

            val intent = Intent(applicationContext, QuizingActivity::class.java)
            intent.putExtra(QuizingActivity.BROADCAST_ID, this)
            if (userId != SC.USER_ID)
                intent.putExtra(QuizingActivity.IS_ADMIN, false)
            else
                return@run
            startActivity(intent)
        }

    }

    override fun onBackPressed() {
        val tempTime = System.currentTimeMillis()
        val intervalTime = tempTime - backPressedTime

        if (0 <= intervalTime && FINISH_INTERVAL_TIME >= intervalTime) {
            super.onBackPressed()
        } else {
            backPressedTime = tempTime
            Toast.makeText(applicationContext, "한번 더 뒤로가기 누르면 앱이 종료됩니다", Toast.LENGTH_SHORT).show()
        }

    }


    companion object {
        const val BROADCAST_ID = "BROADCAST_ID"
        const val USER_ID = "USER_ID"
    }

}
