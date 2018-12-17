package com.quiz_together.ui.deprecated.publish

import com.quiz_together.data.Repository


class PublishPresenter(
        private val repository: Repository,
        private val view: PublishContract.View
) : PublishContract.Presenter {

    init {
        view.presenter = this
    }

    override fun start() {

    }



}