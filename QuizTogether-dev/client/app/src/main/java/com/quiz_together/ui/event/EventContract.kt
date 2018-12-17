package com.quiz_together.ui.event

import com.quiz_together.BasePresenter
import com.quiz_together.BaseView
import com.quiz_together.data.model.Event
import com.quiz_together.data.model.Events

interface EventContract {

    interface View : BaseView<Presenter> {

        fun setLoadingIndicator(active: Boolean)

        val isActive: Boolean

        fun showEvents(events:List<Event>)
    }

    interface Presenter : BasePresenter {

        fun loadEvents()
    }


}
