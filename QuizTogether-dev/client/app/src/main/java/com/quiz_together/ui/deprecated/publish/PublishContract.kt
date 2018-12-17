package com.quiz_together.ui.deprecated.publish

import com.quiz_together.BasePresenter
import com.quiz_together.BaseView


interface PublishContract {

    interface View : BaseView<Presenter> {

        fun setLoadingIndicator(active: Boolean)
        var isActive: Boolean

    }

    interface Presenter : BasePresenter {

    }


}
