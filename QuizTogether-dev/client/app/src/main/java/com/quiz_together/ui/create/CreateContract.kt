package com.quiz_together.ui.create

import com.quiz_together.BasePresenter
import com.quiz_together.BaseView


interface CreateContract {

    interface View : BaseView<Presenter> {

        fun setLoadingIndicator(active: Boolean)
        var isActive: Boolean

    }

    interface Presenter : BasePresenter {

    }


}
