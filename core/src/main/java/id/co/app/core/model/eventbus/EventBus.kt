package id.co.app.core.model.eventbus

import androidx.lifecycle.LiveData
import id.co.app.core.base.SingleLiveEvent

class EventBus {
    private val _events = SingleLiveEvent<DataEvent>()
    val events: LiveData<DataEvent> get() = _events

    fun invokeEvent(event: DataEvent) {
        _events.postValue(event) // suspends until all subscribers receive it
    }
}