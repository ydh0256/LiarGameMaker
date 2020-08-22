package com.duckkite.android.liargamemaker.ui.base

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.duckkite.android.liargamemaker.util.extention.disableUserInteraction
import com.duckkite.android.liargamemaker.util.extention.enableUserInteraction

abstract class BaseFragment: Fragment() {
    abstract fun getViewModel(): BaseViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getViewModel().isLoading.observe(this.viewLifecycleOwner, Observer {isLoading ->
            when(isLoading) {
                true -> disableUserInteraction()
                false -> enableUserInteraction()
            }
        })
    }
}