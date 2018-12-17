package com.quiz_together.ui.base

import android.util.Log
import com.quiz_together.data.Repository
import com.quiz_together.data.model.UserView
import com.quiz_together.data.remote.ApiHelper
import com.quiz_together.util.SC


class LoadingPresenter(
        private val repository: Repository,
        private val view: LoadingContract.View
) : LoadingContract.Presenter {

    val TAG = "LoadingPresenter"

    init {
        view.presenter = this
    }

    override fun start() {
        view.setLoadingIndicator(true)

//        Handler().postDelayed({
            checkHasId()
//        }, 1)

    }

    fun checkHasId() {

        view.setLoadingIndicator(true)

        repository.getUserId()?.let {

            repository.login(it, object :ApiHelper.UserViewCallback {

                override fun onLoginLoaded(respLogin: UserView) {
                    view.run {
                        if(!isActive) return@onLoginLoaded
                        setLoadingIndicator(false)

                        SC.USER_ID = respLogin.userId
                        SC.USER_NAME = respLogin.name

                        showMainUi()
                    }
                }
                override fun onDataNotAvailable() {
                    view.run {
                        if(!isActive) return@onDataNotAvailable
                        setLoadingIndicator(false)

                        showErrorTxt()
                    }

                }

            })

        }?:let {
            view.run {
                if(!isActive) return@run
                setLoadingIndicator(false)

                showLoginUi()
            }
        }

    }




}