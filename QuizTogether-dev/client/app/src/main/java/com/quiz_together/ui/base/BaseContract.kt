package com.quiz_together.ui.base

import com.quiz_together.BasePresenter
import com.quiz_together.BaseView


interface BaseContract {

    interface View : BaseView<Presenter> {

        fun setLoadingIndicator(active: Boolean)
        var isActive: Boolean

    }

    interface Presenter : BasePresenter {

    }


}
