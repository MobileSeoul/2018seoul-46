package com.quiz_together.ui.base

import com.quiz_together.data.Repository


class BasePresenter(
        private val repository: Repository,
        private val view: BaseContract.View
) : BaseContract.Presenter {

    init {
        view.presenter = this
    }

    override fun start() {

    }



}