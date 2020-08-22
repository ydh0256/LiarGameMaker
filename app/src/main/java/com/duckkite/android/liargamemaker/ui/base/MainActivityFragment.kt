package com.duckkite.android.liargamemaker.ui.base

import android.view.View
import androidx.fragment.app.Fragment
import com.duckkite.android.liargamemaker.ui.main.MainActivity
import kotlinx.android.synthetic.main.activity_main.*

abstract class MainActivityFragment : BaseFragment() {
    fun hideFabButton() {
        (activity as? MainActivity)?.let {
            it.fabStartGame.visibility = View.GONE
        }
    }

    fun showFabButton() {
        (activity as? MainActivity)?.let {
            it.fabStartGame.visibility = View.VISIBLE
        }
    }
}