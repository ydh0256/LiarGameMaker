package com.duckkite.android.liargamemaker.ui.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.duckkite.android.liargamemaker.util.extention.disableUserInteraction
import com.duckkite.android.liargamemaker.util.extention.enableUserInteraction

abstract class BaseActivity: AppCompatActivity() {
    abstract fun getViewModel(): BaseViewModel?

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        getViewModel()?.isLoading?.observe(this, Observer {isLoading ->
            when(isLoading) {
                true -> disableUserInteraction()
                false -> enableUserInteraction()
            }
        })
    }
}