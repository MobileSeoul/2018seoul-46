package com.quiz_together.ui.event

import android.util.Log
import com.quiz_together.data.Repository
import com.quiz_together.data.model.Event
import com.quiz_together.data.model.Events
import com.quiz_together.data.remote.ApiHelper

class EventPresenter(
        private val id: String,
        private val repository: Repository,
        private val eventView: EventContract.View
) : EventContract.Presenter {

    val TAG = "EventPresenter"

    init {
        eventView.presenter = this
    }

    override fun start() {

        var I_CAN_RECEIVE_ID = id
        Log.i(TAG,"I_CAN_RECEIVE_ID ${I_CAN_RECEIVE_ID}")

        loadEvents()
    }

    override fun loadEvents() {

        repository.getEvents(object : ApiHelper.GetEventsCallback {
            override fun onEventsLoaded(events: Events) {
                eventView.run{
                    if(!isActive) return@onEventsLoaded

                    setLoadingIndicator(false)

                    showEvents(events.events)
                }
            }

            override fun onDataNotAvailable() {
                eventView.run {
                    if (!isActive) return@onDataNotAvailable

                    setLoadingIndicator(false)
                }
            }

        })


    }


}