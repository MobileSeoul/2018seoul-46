package com.quiz_together.ui.create

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.DialogFragment
import android.os.Bundle
import java.util.*

class DatePickerDialogFragment : DialogFragment() {

    lateinit var mOnDateSetListener: DatePickerDialog.OnDateSetListener

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        return DatePickerDialog(activity, mOnDateSetListener, year, month, day)
    }

    fun setOnTimeSetListener(listener: DatePickerDialog.OnDateSetListener) {
        mOnDateSetListener = listener
    }
}