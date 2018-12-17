package com.quiz_together.ui.setting

import android.os.Bundle
import android.preference.PreferenceFragment
import android.support.design.widget.FloatingActionButton
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.quiz_together.R
import com.quiz_together.data.Repository

class SettingFragment : PreferenceFragment() {

    val TAG = "SettingFragment"

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        addPreferencesFromResource(R.xml.app_preferences)

        val keywordScreen = findPreference("key1");

        keywordScreen.setOnPreferenceClickListener { view ->
            Log.i(TAG,"key1")
            true
        }

        findPreference("key2").setOnPreferenceClickListener { v ->
            Repository.setIsFirst(true)
            true
        }

        activity?.findViewById<FloatingActionButton>(R.id.fab)?.setOnClickListener {
            Log.i(TAG,"FloatingActionButton")
        }


    }


}