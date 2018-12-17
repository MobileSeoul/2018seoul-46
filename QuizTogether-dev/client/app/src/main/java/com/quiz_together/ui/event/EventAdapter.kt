package com.quiz_together.ui.event

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_event.view.*
import com.quiz_together.R
import com.quiz_together.data.model.Event

class EventAdapter(private val context: Context?, val cb: (str:String) -> Unit ) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val list = mutableListOf<Event>()

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as? ImageViewHolder)?.onBind(list[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder
        = ImageViewHolder(context, parent,cb)

    override fun getItemCount() = list.size

    fun addItem(data: Event) = list.add(data)

    fun clearItem() = list.clear()

    fun notifyDataSetChang() = notifyDataSetChanged()

    class ImageViewHolder(context: Context?, parent: ViewGroup?,val cbOnClickLl: (str: String) -> Unit)
        : RecyclerView.ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_event, parent, false)) {

        fun onBind(item: Event) {
            itemView.onBind(item)
        }

        private fun View.onBind(item: Event) {

            tvId.text = item.id.toString()
            tvThumbnail.text = item.thumbnail
            tvTopic.text = item.topics

            ll.setOnClickListener({ _ ->
                cbOnClickLl.invoke(item.id.toString() + item.thumbnail + item.topics)
            })


        }

    }



}