package com.quiz_together.ui.quizing

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Window
import com.quiz_together.R
import com.quiz_together.data.Repository
import com.quiz_together.data.remote.FirebaseHelper
import com.quiz_together.ui.SelectorDialog
import com.quiz_together.util.replaceFragmentInActivity


class QuizingActivity : AppCompatActivity() {

    val TAG = "QuzingActi#$#"

    lateinit var quizingPresenter:QuizingPresenter

    var isAdmin = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_quizing)

        val broadcastId = intent.getStringExtra(BROADCAST_ID)

        isAdmin = intent.getBooleanExtra(IS_ADMIN,false)

        val fragment = supportFragmentManager
                .findFragmentById(R.id.fl_content) as QuizingFragment? ?:
        QuizingFragment.newInstance(isAdmin).also {

            replaceFragmentInActivity(it, R.id.fl_content)
        }

        quizingPresenter = QuizingPresenter(broadcastId,isAdmin,Repository ,fragment)

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

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private val mMessageReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {

            val fcmMsg = intent.getStringExtra(FirebaseHelper.FMC_IN_QUIZING)
            Log.i(TAG, "onReceive : $fcmMsg")
//            Toast.makeText(applicationContext, fcmMsg, Toast.LENGTH_LONG).show()

            quizingPresenter.onFcmListener(fcmMsg)

        }
    }

    companion object {
        const val BROADCAST_ID = "BROADCAST_ID"
        //        const val LAST_QUESTION_NUM = "LAST_QUESTION_NUM"
        const val IS_ADMIN = "IS_ADMIN"
    }

    override fun onBackPressed() {

        SelectorDialog(this,
                title = "퀴즈를 정말로 종료하시겠습니까?",
                firstSelector = SelectorDialog.DialogSelectorInfo("네, 종료할게요", R.color.soDeepBlue),
                secondSelector = SelectorDialog.DialogSelectorInfo("한 번 더 확인해볼게요!", R.color.redInDialog),
                cb = {
                    when(it) {
                        1 -> {
                            quizingPresenter.unsubscribeFirebase(true)
                            if(isAdmin) quizingPresenter.endBroadcast()
                            else finish()
                        }
                    }
                }).create()


    }


}
