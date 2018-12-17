package com.quiz_together.ui.login

import android.app.Activity
import com.quiz_together.BasePresenter
import com.quiz_together.BaseView

interface LoginContract {

    interface View : BaseView<Presenter> {

        fun setLoadingIndicator(active: Boolean)
        var isActive: Boolean
        fun showLoadingUi()
        fun isCheckSuccess(isSuccess:Boolean)
        fun showFailLoginTxt()

        fun getAct() : Activity

    }

    interface Presenter : BasePresenter {
        fun signupTask(name: String )
        fun checkTask(id:String)

    }


}
