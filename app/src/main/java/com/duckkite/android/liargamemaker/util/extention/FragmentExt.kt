package com.duckkite.android.liargamemaker.util.extention

import android.view.WindowManager
import androidx.fragment.app.Fragment

fun Fragment.disableUserInteraction() {
    activity?.window?.setFlags(
        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
    )
}

fun Fragment.enableUserInteraction() {
    activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
}