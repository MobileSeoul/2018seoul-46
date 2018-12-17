package com.quiz_together.ui.quizing

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.quiz_together.R

class QuizingAdapter(val context:Context) : BaseAdapter() {

    val TAG = "QuizingAdapter#$#"

    var users = mutableListOf<Pair<String,String>>()

    override fun getItem(position: Int): Any {
        return users.get(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return users.size
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        var inflator = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        var retView = inflator.inflate(R.layout.item_quizing_user, null)
        val tvName1 = retView.findViewById<TextView>(R.id.tvName1)
        val tvName2 = retView.findViewById<TextView>(R.id.tvName2)
        val ivProfile2 = retView.findViewById<ImageView>(R.id.ivProfile2)
        val ivProfile2Crown = retView.findViewById<ImageView>(R.id.ivProfile2Crown)

        tvName1.text = this.users.get(position).first
        tvName2.text = this.users.get(position).second

        if (this.users.get(position).second.isNullOrEmpty()) {
            ivProfile2.visibility = View.INVISIBLE
            ivProfile2Crown.visibility = View.INVISIBLE
        }

        return retView
    }



}