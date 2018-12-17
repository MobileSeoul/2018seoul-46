package com.quiz_together.ui.main.profile

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import com.quiz_together.App
import com.quiz_together.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragm_profile.*

class ProfileFragment : Fragment(), ProfileContract.View {

    private val TAG = "ProfileFragment#$#"
    private lateinit var profilePresenter : ProfilePresenter




    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
            = inflater?.inflate(R.layout.fragm_profile, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        initView()

    }

    private fun initView() {
        profilePresenter = ProfilePresenter(this@ProfileFragment, pb)

        rcpbExp.apply {
            secondaryProgress = 100F
            max = 100F
            progressBackgroundColor = ContextCompat.getColor(App.instance.applicationContext, R.color.rcpbColorBorder)

            progressColor = ContextCompat.getColor(App.instance.applicationContext, R.color.deepBlue)
            progress = 60F; // default value
        }





    }



}
