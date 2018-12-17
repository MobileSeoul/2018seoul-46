package com.quiz_together.ui.quizing

import android.util.Log
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.JsonObject
import com.quiz_together.data.Repository
import com.quiz_together.data.model.AdminMsg
import com.quiz_together.data.model.AnswerMsg
import com.quiz_together.data.model.BroadcastJoinInfo
import com.quiz_together.data.model.BroadcastStatus
import com.quiz_together.data.model.ChatMsg
import com.quiz_together.data.model.EndMsg
import com.quiz_together.data.model.PushType
import com.quiz_together.data.model.QuestionMsg
import com.quiz_together.data.model.ReqEndBroadcast
import com.quiz_together.data.model.ReqSendAnswer
import com.quiz_together.data.model.ReqStartBroadcast
import com.quiz_together.data.model.ResStartBroadcast
import com.quiz_together.data.model.WinnersMsg
import com.quiz_together.data.remote.ApiHelper
import com.quiz_together.util.SC

class QuizingPresenter(
        private val broadcastId:String,
        private val isAdmin:Boolean,
        private val repository: Repository,
        private val view: QuizingContract.View
) : QuizingContract.Presenter {


    val TAG = "QuizingPresenter#$#"

    init {
        view.presenter = this
    }

    override fun start() {

        registFirbaseSubscribe()

        if (!isAdmin) {
            repository.joinBroadcast(broadcastId, SC.USER_ID, object : ApiHelper.GetJoinBroadcastInfoCallback {
                override fun onJoinBroadcastInfoLoaded(broadcastJoinInfo: BroadcastJoinInfo) {
                    registFirbaseSubscribe()

                    view.setQuestionCnt(broadcastJoinInfo.broadcastView.questionCount)
                }

                override fun onDataNotAvailable() {
                    Log.i(TAG, "joinBroadcast - onDataNotAvailable")
                    view.endQuizFromErr()
                }

            })

        } else {


            repository.startBroadcast(ReqStartBroadcast(broadcastId, SC.USER_ID, "", ""), object : ApiHelper.GetBroadcastViewCallback {
                override fun onBroadcastViewLoaded(resStartBroadcast: ResStartBroadcast) {
                    view.setQuestionCnt(resStartBroadcast.broadcastView.questionCount)

                    registFirbaseSubscribe()

                }


                override fun onDataNotAvailable() {
                    Log.i(TAG, "startBroadcast - onDataNotAvailable")
                    view.endQuizFromErr()
                }

            })

        }
    }

    fun registFirbaseSubscribe(){

        FirebaseMessaging.getInstance().unsubscribeFromTopic(broadcastId)
        FirebaseMessaging.getInstance().subscribeToTopic(broadcastId).addOnSuccessListener {
            Log.i(TAG,"success regist topic >> ${broadcastId}") // empty same key
        }.addOnCompleteListener {
            Log.i(TAG,"complete regist topic >> ${broadcastId}") // duplicate
            view.initQuizCalledByPresenter()

        }
    }

    fun onFcmListener(fcmMsg:String ) {

        val gsObj = SC.gson.fromJson(fcmMsg,JsonObject::class.java)

        when(gsObj.get("pushType").asString) {
            PushType.ADMIN_MESSAGE.name -> view.showAdminMsg(SC.gson.fromJson(fcmMsg, AdminMsg::class.java))
            PushType.ANSWER_MESSAGE.name -> {
                view.showAnswerView(SC.gson.fromJson(fcmMsg, AnswerMsg::class.java))
            }
            PushType.CHAT_MESSAGE.name -> view.showChatMsg(SC.gson.fromJson(fcmMsg, ChatMsg::class.java))
            PushType.END_MESSAGE.name -> {

                if(!isAdmin)
                    view.endQuiz(SC.gson.fromJson(fcmMsg, EndMsg::class.java))
            }
            PushType.QUESTION_MESSAGE.name -> {
                view.showQuestionView(SC.gson.fromJson(fcmMsg, QuestionMsg::class.java))
//                if(isAdmin) updateBroadcastStatus(BroadcastStatus.OPEN_QUESTION)
            }
            PushType.WINNERS_MESSAGE.name -> view.showWinnerView(SC.gson.fromJson(fcmMsg, WinnersMsg::class.java))
        }
    }

    override fun sendAnswer(step: Int, answerNo: Int) {
        repository.sendAnswer(ReqSendAnswer(step,SC.USER_ID,broadcastId,answerNo),
                object : ApiHelper.GetSuccessCallback{
            override fun onSuccessLoaded() {
                Log.i(TAG,"sendAnswer - onSuccessLoaded()")
            }

            override fun onDataNotAvailable() {
                Log.i(TAG,"sendAnswer - onDataNotAvailable()")
            }

        })

    }

    override fun sendMsg(msg: String) {

        if(isAdmin)
            repository.sendAdminChatMsg( broadcastId,SC.USER_ID,msg,
                    object : ApiHelper.GetSuccessCallback {
                        override fun onSuccessLoaded() {
                            Log.i(TAG,"sendChatMsg - onSuccessLoaded()")
                        }

                        override fun onDataNotAvailable() {
                            Log.i(TAG,"sendChatMsg - onDataNotAvailable()")
                        }

                    })
        else
            repository.sendChatMsg( broadcastId,SC.USER_ID,msg,
                    object : ApiHelper.GetSuccessCallback {
                        override fun onSuccessLoaded() {
                            Log.i(TAG,"sendChatMsg - onSuccessLoaded()")
                        }

                        override fun onDataNotAvailable() {
                            Log.i(TAG,"sendChatMsg - onDataNotAvailable()")
                        }

                    })

    }

    override fun unsubscribeFirebase(isSendLeaveBroadcast :Boolean ) {
        FirebaseMessaging.getInstance().unsubscribeFromTopic(broadcastId)

        if(isSendLeaveBroadcast)
        {
            //send leaveBroadcast
            repository.leaveBroadcast(broadcastId,SC.USER_ID,
                    object : ApiHelper.GetSuccessCallback{
                        override fun onSuccessLoaded() {
                            Log.i(TAG,"leaveBroadcast onSuccessLoaded")
                        }

                        override fun onDataNotAvailable() {
                            Log.i(TAG,"leaveBroadcast onDataNotAvailable")
                        }
                    })

        }
    }

    override fun updateBroadcastStatus(broadcastStatus: BroadcastStatus) {

        Log.i(TAG,"## updateBroadcastStatus ##")
        Log.i(TAG,"SC.USER_ID : " + SC.USER_ID + "   broadcastId : " + broadcastId + "   broadcastStatus : " + broadcastStatus)

        repository.updateBroadcastStatus(broadcastId,SC.USER_ID,broadcastStatus,
                object : ApiHelper.GetSuccessCallback{
                    override fun onSuccessLoaded() {
                        Log.i(TAG,"updateBroadcastStatus onSuccessLoaded")
                    }

                    override fun onDataNotAvailable() {
                        Log.i(TAG,"updateBroadcastStatus onDataNotAvailable")
                    }
                })

    }

    override fun openAnswer(step: Int) {
        repository.openAnswer(broadcastId,SC.USER_ID,step,
                object : ApiHelper.GetSuccessCallback {
                    override fun onSuccessLoaded() {
                        Log.i(TAG,"openAnswer onSuccessLoaded")
                    }

                    override fun onDataNotAvailable() {
                        Log.i(TAG,"openAnswer onDataNotAvailable")
                    }
                })
    }

    override fun openQuestion(step: Int) {
        repository.openQuestion(broadcastId,SC.USER_ID,step,
                object : ApiHelper.GetSuccessCallback {
                    override fun onSuccessLoaded() {
                        Log.i(TAG,"openQuestion onSuccessLoaded")
                    }

                    override fun onDataNotAvailable() {
                        Log.i(TAG,"openQuestion onDataNotAvailable")
                    }
                })
    }

    override fun openWinners() {
        repository.openWinners(broadcastId,SC.USER_ID,
                object : ApiHelper.GetSuccessCallback {
                    override fun onSuccessLoaded() {
                        Log.i(TAG,"openWinners onSuccessLoaded")
                    }

                    override fun onDataNotAvailable() {
                        Log.i(TAG,"openWinners onDataNotAvailable")
                    }
                })
    }

    override fun endBroadcast() {

        repository.endBroadcast(ReqEndBroadcast(broadcastId,SC.USER_ID,"dummy","dummy"),
                object : ApiHelper.GetSuccessCallback {
                    override fun onSuccessLoaded() {
                        Log.i(TAG,"endBroadcast onSuccessLoaded")
                    }

                    override fun onDataNotAvailable() {
                        Log.i(TAG,"endBroadcast onDataNotAvailable")
                    }
                })
    }


}