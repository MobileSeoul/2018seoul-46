package com.quiz_together.ui.main.home

import com.quiz_together.BasePresenter
import com.quiz_together.data.model.Follower
import com.quiz_together.data.model.ResGetPagingBroadcastList


interface HomeContract {

    interface View {

        fun setLoadingIndicator(active: Boolean)

        val isActive: Boolean

        fun showBroadcasts(resGetPagingBroadcastList: ResGetPagingBroadcastList,followList: List<Follower>)

    }

    interface Presenter: BasePresenter {

        fun loadBroadcasts()

        fun insertFollower(userId:String, followerId:String)
        fun deleteFollower(userId:String, followerId:String)

        fun tmpEndBroadcast(broadcastId: String)
    }
}