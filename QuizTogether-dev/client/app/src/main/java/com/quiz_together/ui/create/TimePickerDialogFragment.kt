package com.quiz_together.ui.create

import android.app.Dialog
import android.app.DialogFragment
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.format.DateFormat
import java.util.*

class TimePickerDialogFragment : DialogFragment() {

    lateinit var mOnTimeSetListener: TimePickerDialog.OnTimeSetListener

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        return TimePickerDialog(activity, mOnTimeSetListener, hour, minute,
                DateFormat.is24HourFormat(activity))
    }

    fun setOnTimeSetListener(listener: TimePickerDialog.OnTimeSetListener) {
        mOnTimeSetListener = listener
    }
}
