package com.quiz_together.ui.base

import com.quiz_together.BasePresenter
import com.quiz_together.BaseView


interface LoadingContract {

    interface View : BaseView<Presenter> {

        fun setLoadingIndicator(active: Boolean)
        var isActive: Boolean
        fun showLoginUi()
        fun showMainUi()
        fun showErrorTxt()
    }

    interface Presenter : BasePresenter {

    }


}
