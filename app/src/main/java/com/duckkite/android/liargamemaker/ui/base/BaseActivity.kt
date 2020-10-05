package com.duckkite.android.liargamemaker.ui.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.duckkite.android.liargamemaker.data.event.ErrorEvent
import com.duckkite.android.liargamemaker.util.extention.disableUserInteraction
import com.duckkite.android.liargamemaker.util.extention.enableUserInteraction
import com.duckkite.android.liargamemaker.util.extention.handleBaseViewModelErrorEvent

abstract class BaseActivity: AppCompatActivity() {
    abstract fun getViewModel(): BaseViewModel?
    protected var customErrorHandler: (ErrorEvent) -> Unit = {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        getViewModel()?.isLoading?.observe(this, Observer {isLoading ->
            when(isLoading) {
                true -> disableUserInteraction()
                false -> enableUserInteraction()
            }
        })

        handleBaseViewModelErrorEvent(getViewModel(), customErrorHandler)
    }
}