package com.quiz_together.ui.create

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.quiz_together.R
import kotlinx.android.synthetic.main.item_number.view.*

class NumberRecyclerViewAdapter(context: Context) : RecyclerView.Adapter<NumberRecyclerViewAdapter.ViewHolder>() {
    private val mInflater: LayoutInflater = LayoutInflater.from(context)
    private val mStates = ArrayList<STATE>()
    private val mNumbers = (1..12).toList()
    private var mCurrentIdx = 0

    lateinit var mItemClickListener: ItemClickLisnter
    lateinit var mFragmentList: ArrayList<QuizInputFragment>

    interface ItemClickLisnter {
        fun onItemClicked(idx: Int)
    }

    init {
        mStates.add(STATE.FOCUSING)
        for (i in 1..11) {
            mStates.add(STATE.DEFAULT)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = mInflater.inflate(R.layout.item_number, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvNumber.text = mNumbers[position].toString()

        var backgroundRes = 0
        var textColor = 0
        when (mStates[position]) {
            STATE.DEFAULT -> {
                backgroundRes = R.drawable.quiz_number_view_state_default
                textColor = ContextCompat.getColor(holder.tvNumber.context, R.color.light_gray)
            }
            STATE.COMPLETED -> {
                backgroundRes = R.drawable.quiz_number_view_state_completed
                textColor = ContextCompat.getColor(holder.tvNumber.context, R.color.cyan)
            }
            STATE.FOCUSING -> {
                backgroundRes = R.drawable.quiz_number_view_state_focusing
                textColor = ContextCompat.getColor(holder.tvNumber.context, R.color.yellow)
            }
        }

        holder.tvNumber.setBackgroundResource(backgroundRes)
        holder.tvNumber.setTextColor(textColor)
    }

    override fun getItemCount(): Int {
        return mNumbers.size
    }

    fun setCurrentItem(currentIdx: Int) {
        mStates[mCurrentIdx] = if (mFragmentList[mCurrentIdx].isCompleted()) STATE.COMPLETED else STATE.DEFAULT
        notifyItemChanged(mCurrentIdx)
        mStates[currentIdx] = STATE.FOCUSING
        notifyItemChanged(currentIdx)
        mCurrentIdx = currentIdx

    }

    fun setItemClickListener(listener: (idx: Int) -> Unit) {
        mItemClickListener = object : ItemClickLisnter {
            override fun onItemClicked(idx: Int) {
                listener(idx)
            }
        }
    }

    enum class STATE { DEFAULT, COMPLETED, FOCUSING }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val tvNumber = itemView.tvNumber
        var currentState = STATE.DEFAULT

        init {
            tvNumber.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            currentState = STATE.FOCUSING
            mItemClickListener.onItemClicked(adapterPosition)
        }
    }
}