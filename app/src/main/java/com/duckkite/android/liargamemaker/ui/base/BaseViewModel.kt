package com.duckkite.android.liargamemaker.ui.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.duckkite.android.liargamemaker.data.event.ActionEvent
import com.duckkite.android.liargamemaker.data.event.ErrorEvent
import com.duckkite.android.liargamemaker.data.event.Event

abstract class BaseViewModel : ViewModel(){
    private val _actionEvent = MutableLiveData<Event<ActionEvent>>()
    val actionEvent: LiveData<Event<ActionEvent>>
        get() = _actionEvent

    private val _errorEvent = MutableLiveData<Event<ErrorEvent>>()
    val errorEvent: LiveData<Event<ErrorEvent>>
        get() = _errorEvent

    private val _isLoading = MutableLiveData<Boolean>().apply { value = false }
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    fun sendActionEvent(actionEvent: ActionEvent) {
        _actionEvent.value = Event(actionEvent)
    }

    fun sendErrorEvent(errorEvent: ErrorEvent) {
        _errorEvent.value = Event(errorEvent)
    }

    fun loadStart() {
        _isLoading.value = true
    }

    fun loadEnd() {
        _isLoading.value = false
    }
}