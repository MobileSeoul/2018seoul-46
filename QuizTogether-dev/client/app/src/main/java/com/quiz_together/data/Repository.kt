package com.quiz_together.data

import com.quiz_together.data.local.AppPreferenceHelper
import com.quiz_together.data.local.PreferenceHelper
import com.quiz_together.data.model.*
import com.quiz_together.data.remote.ApiHelper
import com.quiz_together.data.remote.AppApiHelper


object Repository : PreferenceHelper, ApiHelper {

    private val preferenceHelper = AppPreferenceHelper()
    private val apiHelper = AppApiHelper()

    // shared prefrenece

    override fun isFirstLaunch(): Boolean {
        return preferenceHelper.isFirstLaunch()
    }

    override fun setIsFirst(isFirst: Boolean) {
        preferenceHelper.setIsFirst(isFirst)
    }

    override fun setUserId(userId: String) {
        preferenceHelper.setUserId(userId)
    }

    override fun getUserId(): String? = preferenceHelper.getUserId()


    // rest api


    override fun signup(name: String, pushToken: String, cb: ApiHelper.UserViewCallback) {
        apiHelper.signup(name,pushToken,cb)
    }

    override fun login(name: String, cb: ApiHelper.UserViewCallback) {
        apiHelper.login(name,cb)
    }

    override fun findUserByName(name: String, cb: ApiHelper.GetSuccessCallback) {
        apiHelper.findUserByName(name,cb)
    }

    override fun getUserProfile(userId: String, cb: ApiHelper.GetUserCallback) {
        apiHelper.getUserProfile(userId,cb)
    }

    override fun createBroadcast(broadcast: Broadcast, cb: ApiHelper.GetSuccessBroadcastIdCallback) {
        apiHelper.createBroadcast(broadcast,cb)
    }

    override fun getPagingBroadcastList(userId: String, cb: ApiHelper.GetPagingBroadcastList) {
        apiHelper.getPagingBroadcastList(userId, cb)
    }

    override fun getBroadcastById(broadcastId: String, cb: ApiHelper.GetBroadcastCallback) {
        apiHelper.getBroadcastById(broadcastId,cb)
    }

    override fun updateBroadcast(broadcast: Broadcast, cb: ApiHelper.GetSuccessCallback) {
        apiHelper.updateBroadcast(broadcast,cb)
    }

    override fun sendAnswer(reqSendAnswer: ReqSendAnswer, cb: ApiHelper.GetSuccessCallback) {
        apiHelper.sendAnswer(reqSendAnswer,cb)
    }

    override fun endBroadcast(reqEndBroadcast: ReqEndBroadcast, cb: ApiHelper.GetSuccessCallback) {
        apiHelper.endBroadcast(reqEndBroadcast,cb)
    }

    /**
     * 방입장 할때 호출
     */
    override fun startBroadcast(reqStartBroadcast: ReqStartBroadcast, cb: ApiHelper.GetBroadcastViewCallback) {
        apiHelper.startBroadcast(reqStartBroadcast,cb)
    }

    override fun getBroadcastForUpdateById(broadcastId: String, cb: ApiHelper.GetBroadcastCallback) {
        apiHelper.getBroadcastForUpdateById(broadcastId,cb)
    }

    override fun getEvents(cb: ApiHelper.GetEventsCallback) {
        apiHelper.getEvents(cb)
    }

    override fun joinBroadcast(broadcastId: String, userId: String, cb: ApiHelper.GetJoinBroadcastInfoCallback) {
        apiHelper.joinBroadcast(broadcastId,userId,cb)
    }

    override fun sendChatMsg(broadcastId: String, userId: String, msg: String, cb: ApiHelper.GetSuccessCallback) {
        apiHelper.sendChatMsg(broadcastId,userId ,msg,cb)
    }

    override fun sendAdminChatMsg(broadcastId: String, userId: String, msg: String, cb: ApiHelper.GetSuccessCallback) {
        apiHelper.sendAdminChatMsg(broadcastId,userId ,msg,cb)
    }

    override fun updateBroadcastStatus(broadcastId: String, userId: String, broadcastStatus: BroadcastStatus, cb: ApiHelper.GetSuccessCallback) {
        apiHelper.updateBroadcastStatus(broadcastId,userId,broadcastStatus,cb)
    }

    override fun leaveBroadcast(broadcastId: String, userId: String, cb: ApiHelper.GetSuccessCallback) {
        apiHelper.leaveBroadcast(broadcastId,userId,cb)
    }

    override fun openWinners(broadcastId: String, userId: String, cb: ApiHelper.GetSuccessCallback) {
        apiHelper.openWinners(broadcastId,userId,cb)
    }

    /**
     * 문제 제출 시 호출
     */
    override fun openQuestion(broadcastId: String, userId: String, step: Int, cb: ApiHelper.GetSuccessCallback) {
        apiHelper.openQuestion(broadcastId,userId,step,cb)
    }

    override fun openAnswer(broadcastId: String, userId: String, step: Int, cb: ApiHelper.GetSuccessCallback) {
        apiHelper.openAnswer(broadcastId,userId,step,cb)
    }

    override fun insertFollower(userId: String, followerId: String, cb: ApiHelper.GetSuccessCallback) {
        apiHelper.insertFollower(userId, followerId, cb)
    }

    override fun deleteFollower(userId: String, followerId: String, cb: ApiHelper.GetSuccessCallback) {
        apiHelper.deleteFollower(userId, followerId, cb)
    }

    override fun getFollowerList(userId: String, cb: ApiHelper.GetFollowerListCallback) {
        apiHelper.getFollowerList(userId, cb)
    }



    override fun hasSavedQuiz(): Boolean = preferenceHelper.hasSavedQuiz()
    override fun saveQuiz(incompletedBroadcast: Broadcast) = preferenceHelper.saveQuiz(incompletedBroadcast)
    override fun getSavedQuiz(): Broadcast? = preferenceHelper.getSavedQuiz()
    override fun getSavedFollowerList(): List<String> = preferenceHelper.getSavedFollowerList()
    override fun setFollowerList(setStr: Set<String>) = preferenceHelper.setFollowerList(setStr)


}
