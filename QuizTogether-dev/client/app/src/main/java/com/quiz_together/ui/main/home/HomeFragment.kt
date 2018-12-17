package com.quiz_together.ui.main.home

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.*
import android.widget.ImageButton
import com.quiz_together.R
import com.quiz_together.data.Repository
import com.quiz_together.data.model.Follower
import com.quiz_together.data.model.ResGetPagingBroadcastList
import com.quiz_together.data.model.RoomOutputType
import com.quiz_together.ui.create.CreateActivity
import com.quiz_together.ui.quizing.QuizingActivity
import com.quiz_together.util.SC
import com.quiz_together.util.setTouchable
import kotlinx.android.synthetic.main.fragm_home.*

class HomeFragment : Fragment(), HomeContract.View {

    private val TAG = "HomeFragment#$#"
    private lateinit var presenter : HomePresenter

    override var isActive: Boolean = false
        get() = isAdded


    private val broadcastAdapter: BroadcastAdapter by lazy {
        BroadcastAdapter(activity?.applicationContext, {

            cbType , broadcast ->

            when (cbType) {
                BroadcastAdapter.CallBackType.ROOM -> {
                    val intent = Intent(activity?.applicationContext, QuizingActivity::class.java)
                    intent.putExtra(QuizingActivity.BROADCAST_ID, broadcast.broadcastId)
                    intent.putExtra(QuizingActivity.IS_ADMIN, if (broadcast.roomOutputType == RoomOutputType.RESERVATION) true else false)
                    // TODO Create RESERVED 로 진입할때 처리 RESERVED intent 넣어서
                    startActivity(intent)
                }
                BroadcastAdapter.CallBackType.FOLLOW -> presenter.insertFollower(SC.USER_ID,broadcast.userInfoView!!.userId)
                BroadcastAdapter.CallBackType.UNFOLLOW -> presenter.deleteFollower(SC.USER_ID,broadcast.userInfoView!!.userId)
                BroadcastAdapter.CallBackType.LONG_TOUCH -> {
                    presenter.tmpEndBroadcast(broadcast.broadcastId!!)
                }

            }



        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
            = inflater?.inflate(R.layout.fragm_home, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        initView()

        rvBroadcasts.run {
            adapter = broadcastAdapter
            layoutManager = LinearLayoutManager(activity?.applicationContext)
        }

        ssrl.run{
            scrollUpChild = rvBroadcasts
            setOnRefreshListener { presenter.loadBroadcasts() }
        }
    }

    override fun onResume() {
        super.onResume()

        setLoadingIndicator(true)

        presenter.start()
    }


    private fun initView() {
        presenter = HomePresenter(this@HomeFragment, pb,Repository)

    }


    override fun setLoadingIndicator(active: Boolean) {
        activity?.getWindow()?.setTouchable(active)
        ssrl.isRefreshing = active
    }

    override fun showBroadcasts(resGetPagingBroadcastList: ResGetPagingBroadcastList,followList: List<Follower>) {

        Log.i(TAG,followList.toString())

        val followSet = followList.map {
            it.follower
        }.toSet()

        Log.i(TAG,followSet.toString())


        broadcastAdapter.run {
            clearItem()
            resGetPagingBroadcastList.myBroadcastList?.forEach { addItem(it, RoomOutputType.RESERVATION) }

            resGetPagingBroadcastList.currentBroadcastList?.forEach {

                if ( followSet.contains(it.userInfoView!!.userId) )
                    addItem(it, RoomOutputType.FOLLOW)
                else
                    addItem(it, RoomOutputType.DEFAULT)
            }

            sortPagingList()

            notifyDataSetChang()
        }
    }


}