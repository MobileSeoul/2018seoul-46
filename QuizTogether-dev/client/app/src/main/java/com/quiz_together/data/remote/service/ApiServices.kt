package com.quiz_together.data.remote.service

import com.quiz_together.BuildConfig
import com.quiz_together.data.model.*
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiServices {

    // dummy method
    @GET("/events")
    fun getEvents(): Observable<Resp<Events>>

    // user
    @POST("${BuildConfig.REST_PREFIX}/user/signup")
    fun signup(@Body data: ReqSignup) : Observable<Resp<UserView>>

    @POST("${BuildConfig.REST_PREFIX}/user/login")
    fun login(@Body data: ReqLogin) : Observable<Resp<UserView>>

    @GET("${BuildConfig.REST_PREFIX}/user/findUserByName")
    fun findUserByName(@Query("name") name:String) : Observable<Resp<RespEmpty>>

    @GET("${BuildConfig.REST_PREFIX}/user/getUserProfile")
    fun getUserProfile(@Query("userId") userId:String) : Observable<Resp<User>>

    // broadcast
    @POST("${BuildConfig.REST_PREFIX}/broadcast/createBroadcast")
    fun createBroadcast(@Body data: Broadcast): Observable<Resp<String>>

    @GET("${BuildConfig.REST_PREFIX}/broadcast/getPagingBroadcastList")
    fun getPagingBroadcastList(@Query("userId") userId: String): Observable<Resp<ResGetPagingBroadcastList>>

    @GET("${BuildConfig.REST_PREFIX}/broadcast/getBroadcastList")
    fun getBroadcastById(@Query("broadcastId") broadcastId:String) : Observable<Resp<Broadcast>>

    @POST("${BuildConfig.REST_PREFIX}/broadcast/updateBroadcast")
    fun updateBroadcast(@Body data: Broadcast) : Observable<Resp<Broadcast>>

    @POST("${BuildConfig.REST_PREFIX}/broadcast/sendAnswer")
    fun sendAnswer(@Body data: ReqSendAnswer) : Observable<Resp<RespEmpty>>

    @POST("${BuildConfig.REST_PREFIX}/broadcast/endBroadcast")
    fun endBroadcast(@Body data: ReqEndBroadcast) : Observable<Resp<RespEmpty>>

    @POST("${BuildConfig.REST_PREFIX}/broadcast/startBroadcast")
    fun startBroadcast(@Body data: ReqStartBroadcast): Observable<Resp<ResStartBroadcast>>

    @POST("${BuildConfig.REST_PREFIX}/broadcast/leaveBroadcast")
    fun leaveBroadcast(@Body data: ReqBrdIdAndUsrId) : Observable<Resp<RespEmpty>>

    @GET("${BuildConfig.REST_PREFIX}/broadcast/getBroadcastForUpdateById")
    fun getBroadcastForUpdateById(@Query("broadcastId") broadcastId:String) : Observable<Resp<Broadcast>>

    @GET("${BuildConfig.REST_PREFIX}/broadcast/joinBroadcast")
    fun joinBroadcast(@Query("broadcastId") broadcastId:String,
                      @Query("userId") userId:String) : Observable<Resp<BroadcastJoinInfo>>

    @GET("${BuildConfig.REST_PREFIX}/follower/getFollowerList")
    fun getFollowerListById(@Query("userId") userId:String) : Observable<Resp<ResFollowList>>


    @POST("${BuildConfig.REST_PREFIX}/broadcast/updateBroadcastStatus")
    fun updateBroadcastStatus(@Body data: ReqUpdateBroadcast) : Observable<Resp<RespEmpty>>

    @POST("${BuildConfig.REST_PREFIX}/firebase/sendChatMessage")
    fun sendChatMsg(@Body data: ReqSendChatMsg) : Observable<Resp<RespEmpty>>

    @POST("${BuildConfig.REST_PREFIX}/firebase/sendAdminChatMessage")
    fun sendAdminChatMsg(@Body data: ReqSendChatMsg) : Observable<Resp<RespEmpty>>

    @POST("${BuildConfig.REST_PREFIX}/firebase/openWinners")
    fun openWinners(@Body data: ReqBrdIdAndUsrId) : Observable<Resp<RespEmpty>>

    @POST("${BuildConfig.REST_PREFIX}/firebase/openQuestion")
    fun openQuestion(@Body data: ReqOpenAnsAndQus) : Observable<Resp<RespEmpty>>

    @POST("${BuildConfig.REST_PREFIX}/firebase/openAnswer")
    fun openAnswer(@Body data: ReqOpenAnsAndQus) : Observable<Resp<RespEmpty>>

    @POST("${BuildConfig.REST_PREFIX}/follower/insertFollower")
    fun insertFollower(@Body data:ReqFollow) : Observable<Resp<RespEmpty>>

    @POST("${BuildConfig.REST_PREFIX}/follower/deleteFollower")
    fun deleteFollower(@Body data:ReqFollow) : Observable<Resp<RespEmpty>>


    companion object Factory {
        fun create(): ApiServices {
            val retrofit = Retrofit.Builder()
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(BuildConfig.SERVER_URL)
                    .build()

            return retrofit.create(ApiServices::class.java);
        }
    }

}
