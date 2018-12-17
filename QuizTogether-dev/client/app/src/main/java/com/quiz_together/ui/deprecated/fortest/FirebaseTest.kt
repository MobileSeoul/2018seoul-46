package com.quiz_together.ui.deprecated.fortest

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import android.view.View
import com.google.firebase.messaging.FirebaseMessaging
import com.quiz_together.R
import com.quiz_together.data.remote.FirebaseHelper
import kotlinx.android.synthetic.main.activity_firebase_test.*

class FirebaseTest : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_firebase_test)




    }


    fun btRegistFbKey(v: View) {

        val fbKey = etFbKey.text.toString()

        if(fbKey.isNullOrEmpty()) return

        FirebaseMessaging.getInstance().unsubscribeFromTopic(fbKey)
        FirebaseMessaging.getInstance().subscribeToTopic(fbKey).addOnSuccessListener {
            tvLog.setText("${fbKey} regist success !! 1")
        }.addOnCompleteListener {
            tvLog.setText("${fbKey} regist success !! 2")
        }

    }

    override fun onResume() {
        super.onResume()

        LocalBroadcastManager.getInstance(this).registerReceiver(
                mMessageReceiver, IntentFilter(FirebaseHelper.FMC_ACTION))
    }

    override fun onPause() {
        super.onPause()

        LocalBroadcastManager.getInstance(this).unregisterReceiver(
                mMessageReceiver)
    }

    private val mMessageReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {

            val fcmMsg = intent.getStringExtra(FirebaseHelper.FMC_IN_QUIZING)

            tvLog.text = fcmMsg

        }
    }
}
