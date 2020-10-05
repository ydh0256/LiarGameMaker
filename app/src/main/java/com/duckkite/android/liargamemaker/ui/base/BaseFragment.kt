package com.duckkite.android.liargamemaker.ui.base

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.duckkite.android.liargamemaker.data.event.ErrorEvent
import com.duckkite.android.liargamemaker.util.extention.disableUserInteraction
import com.duckkite.android.liargamemaker.util.extention.enableUserInteraction
import com.duckkite.android.liargamemaker.util.extention.handleBaseViewModelErrorEvent

abstract class BaseFragment: Fragment() {
    abstract fun getViewModel(): BaseViewModel
    protected var customErrorHandler: (ErrorEvent) -> Unit = {}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getViewModel().isLoading.observe(this.viewLifecycleOwner, Observer {isLoading ->
            when(isLoading) {
                true -> disableUserInteraction()
                false -> enableUserInteraction()
            }
        })

        (activity as? BaseActivity)?.let {
            it.handleBaseViewModelErrorEvent(getViewModel(), customErrorHandler)
        }
    }
}