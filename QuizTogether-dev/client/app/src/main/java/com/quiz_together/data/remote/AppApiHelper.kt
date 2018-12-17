package com.quiz_together.data.remote

import android.util.Log
import com.google.gson.Gson
import com.quiz_together.data.model.*
import com.quiz_together.data.remote.service.ApiServices
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class AppApiHelper : ApiHelper {

    val TAG = "AppApiHelper#$#"

    val apiServices = ApiServices.create()

    // dummy method
    override fun getEvents(cb: ApiHelper.GetEventsCallback) {

        apiServices.getEvents()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ it ->

                    if(it.code == 200)
                        cb.onEventsLoaded(it.data)
                    else
                        cb.onDataNotAvailable()

                }, { _ ->
                    cb.onDataNotAvailable()
                })

    }

    /////



    override fun signup(name: String ,pushToken :String, cb: ApiHelper.UserViewCallback) {

        apiServices.signup(ReqSignup(name,pushToken))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ it ->

                    if(it.code == 200)
                        cb.onLoginLoaded(it.data)
                    else
                        cb.onDataNotAvailable()

                }, { _ ->
                    cb.onDataNotAvailable()
                })


    }

    override fun login(name: String , cb: ApiHelper.UserViewCallback) {

        apiServices.login(ReqLogin(name))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ it ->

                    if(it.code == 200)
                        cb.onLoginLoaded(it.data)
                    else
                        cb.onDataNotAvailable()

                }, { _ ->
                    cb.onDataNotAvailable()
                })


    }

    override fun findUserByName(name: String , cb: ApiHelper.GetSuccessCallback) {
        apiServices.findUserByName(name)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ it ->
                    if(it.code == 200)
                        cb.onSuccessLoaded()
                    else
                        cb.onDataNotAvailable()
                }, { _ ->
                    cb.onDataNotAvailable()
                })
    }

    override fun getUserProfile(userId: String , cb: ApiHelper.GetUserCallback) {
        apiServices.getUserProfile(userId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ it ->
                    if(it.code == 200)
                        cb.onUserLoaded(it.data)
                    else
                        cb.onDataNotAvailable()
                }, { _ ->
                    cb.onDataNotAvailable()
                })
    }

    // broadcast
    override fun createBroadcast(broadcast: Broadcast, cb: ApiHelper.GetSuccessBroadcastIdCallback) {

        apiServices.createBroadcast(broadcast)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ it ->
                    if(it.code == 200)
                        cb.onSuccessLoaded(it.data)
                    else
                        cb.onDataNotAvailable()
                }, { err ->
                    err.printStackTrace()
                    cb.onDataNotAvailable()
                })
    }

    override fun getPagingBroadcastList(userId: String, cb: ApiHelper.GetPagingBroadcastList) {
        apiServices.getPagingBroadcastList(userId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ it ->
                    if(it.code == 200)
                        cb.onPagingBroadcastListLoaded(it.data)
                    else
                        cb.onDataNotAvailable()
                }, { err ->

                    cb.onDataNotAvailable()
                })
    }

    override fun getBroadcastById(broadcastId:String ,cb: ApiHelper.GetBroadcastCallback) {
        apiServices.getBroadcastById(broadcastId )
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ it ->
                    if(it.code == 200)
                        cb.onBroadcastLoaded(it.data)
                    else
                        cb.onDataNotAvailable()
                }, { _ ->
                    cb.onDataNotAvailable()
                })
    }

    override fun updateBroadcast(broadcast: Broadcast, cb: ApiHelper.GetSuccessCallback) {
        apiServices.updateBroadcast(broadcast)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ it ->
                    if(it.code == 200)
                        cb.onSuccessLoaded()
                    else
                        cb.onDataNotAvailable()
                }, { _ ->
                    cb.onDataNotAvailable()
                })
    }

    override fun sendAnswer(reqSendAnswer: ReqSendAnswer, cb: ApiHelper.GetSuccessCallback) {
        apiServices.sendAnswer(reqSendAnswer)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ it ->
                    if(it.code == 200)
                        cb.onSuccessLoaded()
                    else
                        cb.onDataNotAvailable()
                }, { _ ->
                    cb.onDataNotAvailable()
                })
    }

    override fun endBroadcast(reqEndBroadcast: ReqEndBroadcast, cb: ApiHelper.GetSuccessCallback) {
        apiServices.endBroadcast(reqEndBroadcast)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ it ->
                    if(it.code == 200)
                        cb.onSuccessLoaded()
                    else
                        cb.onDataNotAvailable()
                }, { _ ->
                    cb.onDataNotAvailable()
                })
    }

    override fun startBroadcast(reqStartBroadcast: ReqStartBroadcast, cb: ApiHelper.GetBroadcastViewCallback) {
        apiServices.startBroadcast(reqStartBroadcast)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ it ->
//
//                    Log.e(TAG, "startBroadcast rst")
//                    Log.e(TAG, Gson().toJson(it.data))
//                    Log.e(TAG, it.toString())

                    if(it.code == 200)
                        cb.onBroadcastViewLoaded(it.data)
                    else
                        cb.onDataNotAvailable()
                }, { _ ->
                    cb.onDataNotAvailable()
                })
    }

    override fun getBroadcastForUpdateById(broadcastId:String ,cb: ApiHelper.GetBroadcastCallback) {
        apiServices.getBroadcastForUpdateById(broadcastId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ it ->
                    if(it.code == 200)
                        cb.onBroadcastLoaded(it.data)
                    else
                        cb.onDataNotAvailable()
                }, { err ->
                    cb.onDataNotAvailable()
                })
    }

    override fun joinBroadcast(broadcastId: String, userId: String, cb: ApiHelper.GetJoinBroadcastInfoCallback) {

        apiServices.joinBroadcast(broadcastId,userId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ it ->
                    if(it.code == 200)
                        cb.onJoinBroadcastInfoLoaded(it.data)
                    else
                        cb.onDataNotAvailable()
                }, { err ->
                    err.printStackTrace()
                    cb.onDataNotAvailable()
                })

    }

    override fun sendChatMsg(broadcastId: String, userId: String, msg: String, cb: ApiHelper.GetSuccessCallback) {

        apiServices.sendChatMsg(ReqSendChatMsg(broadcastId,userId,msg))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ it ->
                    if(it.code == 200)
                        cb.onSuccessLoaded()
                    else
                        cb.onDataNotAvailable()
                }, { _ ->
                    cb.onDataNotAvailable()
                })

    }

    override fun sendAdminChatMsg(broadcastId: String, userId: String, msg: String, cb: ApiHelper.GetSuccessCallback) {

        apiServices.sendAdminChatMsg(ReqSendChatMsg(broadcastId,userId,msg))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ it ->
                    if(it.code == 200)
                        cb.onSuccessLoaded()
                    else
                        cb.onDataNotAvailable()
                }, { _ ->
                    cb.onDataNotAvailable()
                })

    }

    override fun updateBroadcastStatus(broadcastId: String, userId: String, broadcastStatus: BroadcastStatus, cb: ApiHelper.GetSuccessCallback) {

        apiServices.updateBroadcastStatus(ReqUpdateBroadcast(broadcastId,userId,broadcastStatus))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ it ->
                    if(it.code == 200)
                        cb.onSuccessLoaded()
                    else
                        cb.onDataNotAvailable()
                }, { _ ->
                    cb.onDataNotAvailable()
                })

    }

    override fun leaveBroadcast(broadcastId: String, userId: String, cb: ApiHelper.GetSuccessCallback) {

        apiServices.leaveBroadcast(ReqBrdIdAndUsrId(broadcastId,userId))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ it ->
                    if(it.code == 200)
                        cb.onSuccessLoaded()
                    else
                        cb.onDataNotAvailable()
                }, { _ ->
                    cb.onDataNotAvailable()
                })

    }

    override fun openWinners(broadcastId: String, userId: String, cb: ApiHelper.GetSuccessCallback) {
        apiServices.openWinners(ReqBrdIdAndUsrId(broadcastId,userId))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ it ->
                    if(it.code == 200)
                        cb.onSuccessLoaded()
                    else
                        cb.onDataNotAvailable()
                }, { _ ->
                    cb.onDataNotAvailable()
                })
    }

    override fun openQuestion(broadcastId: String, userId: String, step: Int, cb: ApiHelper.GetSuccessCallback) {

        apiServices.openQuestion(ReqOpenAnsAndQus(broadcastId,userId,step))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ it ->
                    if(it.code == 200)
                        cb.onSuccessLoaded()
                    else
                        cb.onDataNotAvailable()
                }, { _ ->
                    cb.onDataNotAvailable()
                })
    }

    override fun openAnswer(broadcastId: String, userId: String, step: Int, cb: ApiHelper.GetSuccessCallback) {

        apiServices.openAnswer(ReqOpenAnsAndQus(broadcastId,userId,step))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ it ->
                    if(it.code == 200)
                        cb.onSuccessLoaded()
                    else
                        cb.onDataNotAvailable()
                }, { err ->
                    cb.onDataNotAvailable()
                })
    }

    override fun insertFollower(userId: String, followerId: String, cb: ApiHelper.GetSuccessCallback) {

        apiServices.insertFollower(ReqFollow(userId,followerId))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ it ->
                    if(it.code == 200)
                        cb.onSuccessLoaded()
                    else
                        cb.onDataNotAvailable()
                }, { err ->
                    cb.onDataNotAvailable()
                })

    }

    override fun deleteFollower(userId: String, followerId: String, cb: ApiHelper.GetSuccessCallback) {

        apiServices.deleteFollower(ReqFollow(userId,followerId))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ it ->
                    if(it.code == 200)
                        cb.onSuccessLoaded()
                    else
                        cb.onDataNotAvailable()
                }, { err ->
                    cb.onDataNotAvailable()
                })


    }


    override fun getFollowerList(userId: String, cb: ApiHelper.GetFollowerListCallback) {

        apiServices.getFollowerListById(userId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ it ->
                    if(it.code == 200) {

//                        it.data ?.run {
                            cb.onFollowerList(it.data)
//                        } ?: cb.onFollowerList(ResFollowList(listOf<Follower>()))

                    } else {
                        cb.onDataNotAvailable()
                    }
                }, { err ->
                    Log.i(TAG,err.message)
                    err.printStackTrace()
                    cb.onDataNotAvailable()
                })


    }


}