package com.quiz_together.ui

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.support.v4.content.ContextCompat
import android.view.KeyEvent
import android.view.View
import android.view.Window
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.load.model.DataUrlLoader
import com.quiz_together.App
import com.quiz_together.R

/**
 * cb (high order function) return value is
 * select 1,2,3 button -> 1,2,3
 * when pressed back key -> 0
 */
class SelectorDialog(val context :Context,
                     val title:String,
                     val firstSelector:DialogSelectorInfo,
                     val secondSelector:DialogSelectorInfo? = null,
                     val thirdSelector:DialogSelectorInfo? = null,
                     val cb : (Int)->Any
) {

    fun create() {

        val dlg = Dialog(context)

        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dlg.setContentView(R.layout.dialog_common);

        dlg.findViewById<TextView>(R.id.tvTitle).text = title

        val tvSelector1 = dlg.findViewById<TextView>(R.id.tvSelector1)
        val tvSelector2 = dlg.findViewById<TextView>(R.id.tvSelector2)
        val tvSelector3 = dlg.findViewById<TextView>(R.id.tvSelector3)

        val llLine2 = dlg.findViewById<LinearLayout>(R.id.llLine2)
        val llLine3 = dlg.findViewById<LinearLayout>(R.id.llLine3)

        tvSelector1.run {
            text = firstSelector.text
            setTextColor(ContextCompat.getColor(App.instance.applicationContext, firstSelector.colorId))
            setOnClickListener { _ ->
                cb.invoke(1)
                dlg.dismiss()
            }
        }

        if(secondSelector != null)  {
            tvSelector2.run {
                text = secondSelector.text
                setTextColor(ContextCompat.getColor(App.instance.applicationContext, secondSelector.colorId))
                visibility = View.VISIBLE
                setOnClickListener { _ ->
                    cb.invoke(2)
                    dlg.dismiss()
                }
            }
            llLine2.visibility = View.VISIBLE
        }


        if(thirdSelector != null)  {
            tvSelector3.run {
                text = thirdSelector.text
                setTextColor(ContextCompat.getColor(App.instance.applicationContext, thirdSelector.colorId))
                visibility = View.VISIBLE
                setOnClickListener { _ ->
                    cb.invoke(3)
                    dlg.dismiss()
                }

            }
            llLine3.visibility = View.VISIBLE
        }

        dlg.setOnKeyListener { _, keyCode, _ ->

            var ret = false
            if(keyCode == KeyEvent.KEYCODE_BACK) {
                cb.invoke(0)
                dlg.dismiss()
                ret = true
            }
            ret
        }
        dlg.show()
    }

    data class DialogSelectorInfo (
            val text:String,
            val colorId: Int
    )

}