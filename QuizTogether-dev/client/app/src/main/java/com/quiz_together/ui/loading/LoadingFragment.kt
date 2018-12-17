package com.quiz_together.ui.base

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.quiz_together.R
import com.quiz_together.ui.login.LoginActivity
import com.quiz_together.ui.main.MainActivity
import com.quiz_together.util.setTouchable
import com.quiz_together.util.setVisibilityFromBoolean
import kotlinx.android.synthetic.main.frag_base.*

class LoadingFragment : Fragment(), LoadingContract.View {

    val TAG = "LoadingFragment"

    override lateinit var presenter: LoadingContract.Presenter

    override var isActive: Boolean = false
        get() = isAdded

    override fun onResume() {
        super.onResume()
        presenter.start()
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.frag_base, container, false)

        return root
    }

    override fun setLoadingIndicator(active: Boolean) {
        activity?.getWindow()?.setTouchable(active)
        pb.setVisibilityFromBoolean(active)

    }


    override fun showLoginUi() {
        activity?.run{
            startActivity(Intent(applicationContext,LoginActivity::class.java))
            finish()
        }
    }

    override fun showMainUi() {
        activity?.run{
            startActivity(Intent(applicationContext, MainActivity::class.java))
            finish()
        }
    }

    override fun showErrorTxt() {
//        "Network or Server Err, Plase restart application".toast()
    }

    companion object {
        fun newInstance() = LoadingFragment()
    }


}