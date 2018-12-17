package com.quiz_together.data.remote

import com.quiz_together.data.model.*


interface ApiHelper {

    interface GetEventsCallback{
        fun onEventsLoaded(events : Events)
        fun onDataNotAvailable()
    }

    interface UserViewCallback{
        fun onLoginLoaded(respLogin: UserView)
        fun onDataNotAvailable()
    }

    interface GetSuccessCallback{
        fun onSuccessLoaded()
        fun onDataNotAvailable()
    }

    interface GetSuccessBroadcastIdCallback {
        fun onSuccessLoaded(broadcastId: String)
        fun onDataNotAvailable()
    }

    interface GetUserCallback{
        fun onUserLoaded(user: User)
        fun onDataNotAvailable()
    }

    interface GetBroadcastsCallback{
        fun onBroadcastsLoaded(broadcasts: List<Broadcast>)
        fun onDataNotAvailable()
    }

    interface GetPagingBroadcastList {
        fun onPagingBroadcastListLoaded(resGetPagingBroadcastList: ResGetPagingBroadcastList)
        fun onDataNotAvailable()
    }

    interface GetBroadcastCallback{

        fun onBroadcastLoaded(broadcasts: Broadcast)
        fun onDataNotAvailable()
    }

    interface GetBroadcastViewCallback {

        fun onBroadcastViewLoaded(broadcastView: ResStartBroadcast)
        fun onDataNotAvailable()
    }

    interface GetJoinBroadcastInfoCallback{

        fun onJoinBroadcastInfoLoaded(broadcastJoinInfo: BroadcastJoinInfo)
        fun onDataNotAvailable()
    }

    interface GetFollowerListCallback{

        fun onFollowerList(followList: ResFollowList)
        fun onDataNotAvailable()
    }

    // dummy method
    fun getEvents(cb: GetEventsCallback)

    // user
    fun signup(name: String ,pushToken :String, cb: ApiHelper.UserViewCallback)
    fun login(name:String , cb:UserViewCallback)
    fun findUserByName(name: String , cb: ApiHelper.GetSuccessCallback)
    fun getUserProfile(userId: String , cb: ApiHelper.GetUserCallback)

    // broadcast
    fun createBroadcast(broadcast: Broadcast, cb: ApiHelper.GetSuccessBroadcastIdCallback)

    fun getPagingBroadcastList(userId: String, cb: ApiHelper.GetPagingBroadcastList)
    fun getBroadcastById(broadcastId:String ,cb: ApiHelper.GetBroadcastCallback)
    fun updateBroadcast(broadcast: Broadcast, cb: ApiHelper.GetSuccessCallback)
    fun sendAnswer(reqSendAnswer: ReqSendAnswer, cb: ApiHelper.GetSuccessCallback)
    fun endBroadcast(reqEndBroadcast: ReqEndBroadcast, cb: ApiHelper.GetSuccessCallback)
    fun startBroadcast(reqStartBroadcast: ReqStartBroadcast, cb: ApiHelper.GetBroadcastViewCallback)
    fun getBroadcastForUpdateById(broadcastId:String ,cb: ApiHelper.GetBroadcastCallback)
    fun joinBroadcast(broadcastId:String, userId:String, cb: ApiHelper.GetJoinBroadcastInfoCallback)
    fun updateBroadcastStatus(broadcastId: String,userId: String,broadcastStatus: BroadcastStatus, cb: ApiHelper.GetSuccessCallback)
    fun leaveBroadcast(broadcastId: String,userId: String, cb: ApiHelper.GetSuccessCallback)
    fun insertFollower(userId: String, followerId: String,cb :ApiHelper.GetSuccessCallback)
    fun deleteFollower(userId: String, followerId: String,cb :ApiHelper.GetSuccessCallback)
    fun getFollowerList(userId: String,cb :ApiHelper.GetFollowerListCallback)


    //firebase
    fun openWinners(broadcastId:String, userId:String,cb: ApiHelper.GetSuccessCallback)
    fun openQuestion(broadcastId:String, userId:String,step:Int, cb: ApiHelper.GetSuccessCallback)
    fun openAnswer(broadcastId:String, userId:String,step:Int, cb: ApiHelper.GetSuccessCallback)
    fun sendChatMsg(broadcastId:String, userId:String,msg:String, cb: ApiHelper.GetSuccessCallback)
    fun sendAdminChatMsg(broadcastId: String, userId: String, msg: String, cb: ApiHelper.GetSuccessCallback)

}