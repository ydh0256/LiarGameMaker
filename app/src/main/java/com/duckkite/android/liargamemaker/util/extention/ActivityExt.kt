package com.duckkite.android.liargamemaker.util.extention

import android.R
import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.IdRes
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.callbacks.onDismiss
import com.duckkite.android.liargamemaker.data.event.ErrorEvent
import com.duckkite.android.liargamemaker.data.event.ErrorEventType
import com.duckkite.android.liargamemaker.data.event.ErrorEventViewType
import com.duckkite.android.liargamemaker.data.event.Event
import com.duckkite.android.liargamemaker.ui.base.BaseViewModel
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar


fun AppCompatActivity.handleBaseViewModelErrorEvent(
    baseViewModel: BaseViewModel?,
    customErrorHandler: (ErrorEvent) -> Unit = {}
) {
    baseViewModel?.errorEvent?.observe(this, Observer<Event<ErrorEvent>> { event ->
        event.getContentIfNotHandled()?.let { errorEvent ->
            when (errorEvent.errorEventType) {
                ErrorEventType.NORMAL -> normalErrorHandler(this, errorEvent)
                ErrorEventType.CLOSE -> closeErrorHandler(this, errorEvent)
                ErrorEventType.CUSTOM -> customErrorHandler(errorEvent)
            }
        }
    })
}

fun AppCompatActivity.setupActionBar(@IdRes toolbarId: Int, action: ActionBar.() -> Unit) {
    setSupportActionBar(findViewById(toolbarId))
    supportActionBar?.run {
        action()
    }
}

fun FragmentActivity.disableUserInteraction() {
    window.setFlags(
        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
    )
}

fun FragmentActivity.enableUserInteraction() {
    window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
}


private fun Activity.getRootView(): View? {
    val contentViewGroup = findViewById<ViewGroup>(R.id.content)
    contentViewGroup?.let {
        return contentViewGroup.getChildAt(0)
    }
    return window.decorView.rootView
}

private fun normalErrorHandler(activity: Activity, errorEvent: ErrorEvent) {
    when (errorEvent.errorEventViewType) {
        ErrorEventViewType.POPUP -> {
            MaterialDialog(activity).show {
                message(text = getErrorMessage(activity, errorEvent))
            }
        }
        ErrorEventViewType.TOAST -> {
            Toast.makeText(activity, getErrorMessage(activity, errorEvent), Toast.LENGTH_SHORT)
                .show()
        }
        ErrorEventViewType.SNACK_BAR -> {
            activity.getRootView()?.let { view ->
                getErrorMessage(activity, errorEvent)?.let { errorMessage ->
                    Snackbar.make(view, errorMessage, Snackbar.LENGTH_SHORT).show()
                }
            }
        }
    }
}

private fun closeErrorHandler(activity: AppCompatActivity, errorEvent: ErrorEvent) {
    when (errorEvent.errorEventViewType) {
        ErrorEventViewType.POPUP -> {
            MaterialDialog(activity).show {
                message(text = getErrorMessage(activity, errorEvent))
                onDismiss { activity.finish() }
            }
        }
        ErrorEventViewType.TOAST -> {
            Toast.makeText(activity, getErrorMessage(activity, errorEvent), Toast.LENGTH_SHORT)
                .show()
            activity.finish()
        }
        ErrorEventViewType.SNACK_BAR -> {
            val view = activity.getRootView() ?: return
            val errorMessage = getErrorMessage(activity, errorEvent) ?: return
            Snackbar.make(view, errorMessage, Snackbar.LENGTH_SHORT)
                .addCallback(object : BaseTransientBottomBar.BaseCallback<Snackbar>() {
                    override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                        super.onDismissed(transientBottomBar, event)
                        activity.finish()
                    }
                })
                .show()
        }
    }
}

private fun getErrorMessage(activity: Activity, errorEvent: ErrorEvent): String? {
    return errorEvent.stringMessage?.let {
        it
    } ?: run {
        errorEvent.resourceMessage?.let {
            activity.getString(it)
        }
    }
}
