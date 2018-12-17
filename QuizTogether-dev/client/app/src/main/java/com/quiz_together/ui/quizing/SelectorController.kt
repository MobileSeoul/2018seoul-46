package com.quiz_together.ui.quizing

import android.graphics.Color
import android.support.v4.content.ContextCompat
import android.widget.TextView
import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar
import com.quiz_together.App
import com.quiz_together.R

class SelectorController( val RCPBBg : Array<RoundCornerProgressBar> , tvQustionsInfo : Array<TextView>  ) {

    var tvQustions : Array<TextView>
    var tvNumbers : Array<TextView>

    init {
        RCPBBg.forEach { rcpbInit(it) }
        tvQustions = tvQustionsInfo.copyOfRange(0,3)
        tvNumbers = tvQustionsInfo.copyOfRange(3,6)

    }

    fun rcpbInit( rcpb : RoundCornerProgressBar ) {
        rcpb.apply {
            secondaryProgress = 100F
            max = 100F
            progressBackgroundColor = ContextCompat.getColor(App.instance.applicationContext,R.color.rcpbColorBorder)

            progressColor = ContextCompat.getColor(App.instance.applicationContext,SelectorColor.DEFAULT.value) // deafault value
            progress = 0F // default value
        }
    }

    /**
     * pos : 1~3
     * percent : 0 ~ 100
     */
    fun setRCPB(pos: Int, color: SelectorColor = SelectorColor.DEFAULT, percent: Int = 0, withTextColor: Boolean = false) {
        RCPBBg[pos-1].apply {
            progressColor = ContextCompat.getColor(App.instance.applicationContext,color.value)
            progress = percent.toFloat()
        }

        if (withTextColor) tvQustions[pos - 1].setTextColor(ContextCompat.getColor(App.instance.applicationContext, R.color.speciYellow))

    }

    fun setRCPBOnlyColor(pos:Int, color:SelectorColor = SelectorColor.DEFAULT) {
        RCPBBg[pos-1].apply {
            progressColor = ContextCompat.getColor(App.instance.applicationContext,color.value)
        }
    }

    fun setQuestions(str1:String,str2:String,str3:String) {
        tvQustions[0].text = str1
        tvQustions[1].text = str2
        tvQustions[2].text = str3

        tvQustions[0].setTextColor(Color.parseColor("#000000"))
        tvQustions[1].setTextColor(Color.parseColor("#000000"))
        tvQustions[2].setTextColor(Color.parseColor("#000000"))
    }

    fun setNumbers(str1:String,str2:String,str3:String) {
        tvNumbers[0].text = str1
        tvNumbers[1].text = str2
        tvNumbers[2].text = str3
    }

    fun cleanPickNumbers() = tvNumbers.forEach { it.text ="" }



    enum class SelectorColor(val value:Int) {
        DEFAULT(R.color.rcpbColorDefault),
        SELECT(R.color.rcpbColorSelect),
        O(R.color.rcpbColorO),
        X(R.color.rcpbColorX)
    }



}