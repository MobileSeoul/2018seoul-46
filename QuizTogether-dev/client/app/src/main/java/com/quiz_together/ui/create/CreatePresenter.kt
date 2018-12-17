package com.quiz_together.ui.create

import com.quiz_together.data.Repository


class CreatePresenter(
        private val repository: Repository,
        private val view: CreateContract.View
) : CreateContract.Presenter {

    init {
        view.presenter = this
    }

    override fun start() {

    }



}