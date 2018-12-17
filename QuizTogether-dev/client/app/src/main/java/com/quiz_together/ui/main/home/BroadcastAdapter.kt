package com.quiz_together.ui.main.home

import android.content.Context
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.telecom.Call
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.quiz_together.R
import com.quiz_together.data.model.Broadcast
import com.quiz_together.data.model.GiftType
import com.quiz_together.data.model.Question
import com.quiz_together.data.model.RoomOutputType
import com.quiz_together.util.toStringTemplate
import kotlinx.android.synthetic.main.item_home_broadcast.view.*

class BroadcastAdapter(private val context: Context?, val cb: (callBackType : CallBackType,broadcast: Broadcast) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val TAG = "BroadcastAdapter#$#"

    private val list = mutableListOf<Broadcast>()

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as? ImageViewHolder)?.onBind(list[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder
            = ImageViewHolder(context, parent,cb)

    override fun getItemCount() = list.size

    fun addItem(data: Broadcast, roomOutputType: RoomOutputType) {
        data.roomOutputType = roomOutputType
        list.add(data)
    }

    fun sortPagingList() {
        list.
                sortByDescending { it.roomOutputType }
    }

    fun clearItem() = list.clear()

    fun notifyDataSetChang() = notifyDataSetChanged()

    class ImageViewHolder(context: Context?, parent: ViewGroup?, val cbOnClickLl: (callBackType : CallBackType,broadcast: Broadcast) -> Unit)
        : RecyclerView.ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_home_broadcast, parent, false)) {

        val TAG = "ImageViewHolder#$#"

        fun onBind(item: Broadcast) {
            itemView.onBind(item)
        }

        private fun View.onBind(item: Broadcast) {

            tvTitle.text = item.title
            tvDate.text = item.scheduledTime?.toStringTemplate()?.substring(2) + "시작"
            tvName.text = item.userInfoView?.name
            tvPrize.text = item.prize.toString()
            tvContent.text = item.description
            ivProfile.setImageResource(R.drawable.icc_profile)

            val calcedMin = item.remainingStartSeconds!! / 60

            if (item.roomOutputType == RoomOutputType.DEFAULT) {
                ivProfile.borderColor = getResources().getColor(R.color.white)
                llBg.setBackgroundResource(R.drawable.back_white_border_for_layout)
                tvShare.text = "팔로우"

                tvPrize.setTextColor(Color.parseColor("#000000"))
                tvName.setTextColor(Color.parseColor("#000000"))
                tvShare.setTextColor(Color.parseColor("#236ad1"))
                tvTitle.setTextColor(Color.parseColor("#0e8199"))
                tvContent.setTextColor(Color.parseColor("#6b6b6b"))
                tvDate.setTextColor(Color.parseColor("#a2a8b0"))

                tvShare.setOnClickListener({ _ ->
                    cbOnClickLl.invoke(CallBackType.FOLLOW,item)
                })

                rl.setOnLongClickListener { _ ->  false }

            } else if (item.roomOutputType == RoomOutputType.FOLLOW) {
                ivProfile.borderColor = getResources().getColor(R.color.deepBlue)
                llBg.setBackgroundResource(R.drawable.back_deepblue_border_for_layout)
                tvShare.text = "팔로잉"

                tvPrize.setTextColor(resources.getColor(R.color.white))
                tvName.setTextColor(resources.getColor(R.color.white))
                tvShare.setTextColor(resources.getColor(R.color.white))
                tvTitle.setTextColor(Color.parseColor("#fafd47"))
                tvContent.setTextColor(resources.getColor(R.color.white))
                tvDate.setTextColor(Color.parseColor("#fafd47"))
                tvDate.text = "해당 방은 ${calcedMin}분 뒤에 진행되기로 예정되어있습니다."

                tvShare.setOnClickListener({ _ ->
                    cbOnClickLl.invoke(CallBackType.UNFOLLOW,item)
                })

                rl.setOnLongClickListener { _ ->  false }

            } else if (item.roomOutputType == RoomOutputType.RESERVATION) {
                ivProfile.borderColor = getResources().getColor(R.color.shallowDark)
                llBg.setBackgroundResource(R.drawable.back_shallow_dark_border_for_layout)
                tvShare.text = ""

                tvPrize.setTextColor(resources.getColor(R.color.white))
                tvName.setTextColor(resources.getColor(R.color.white))
                tvShare.setTextColor(resources.getColor(R.color.white))
                tvTitle.setTextColor(Color.parseColor("#fafd47"))
                tvContent.setTextColor(resources.getColor(R.color.white))
                tvDate.setTextColor(Color.parseColor("#fafd47"))
                tvDate.text = "준비하신 퀴즈가 ${calcedMin}분 뒤에 진행되기로 예정되어있습니다."

                tvShare.setOnClickListener({ _ -> null
                })


                rl.setOnLongClickListener { _ ->
                    cbOnClickLl.invoke(CallBackType.LONG_TOUCH,item)
                    true }
            }

            rl.setOnClickListener({ _ ->
                cbOnClickLl.invoke(CallBackType.ROOM,item)
            })





        }

    }


    enum class CallBackType(val value:Int) {
        FOLLOW(100),
        UNFOLLOW(150),
        ROOM(200),
        LONG_TOUCH(200),

    }
}