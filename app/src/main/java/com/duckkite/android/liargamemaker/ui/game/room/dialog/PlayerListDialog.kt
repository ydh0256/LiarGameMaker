package com.duckkite.android.liargamemaker.ui.game.room.dialog

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import com.duckkite.android.liargamemaker.data.model.User
import com.duckkite.android.liargamemaker.databinding.DialogUserSelectBinding
import com.duckkite.android.liargamemaker.ui.game.room.adapter.PlayerListAdapter

class PlayerListDialog(private val windowContext: Context) {

    private val bindingView: DialogUserSelectBinding
    var title: String? = null
    var playerList: List<User>? = null
    var payerSelectListener: PlayerListAdapter.PlayerSelectListener? = null
    private var playerListDialog: AlertDialog? = null

    init {
        val layoutInflater = LayoutInflater.from(windowContext)
        bindingView = DialogUserSelectBinding.inflate(layoutInflater, null, false)
    }


    fun show() {
        with(bindingView.playerList) {
            adapter = PlayerListAdapter().apply {
                playerSelectListener = this@PlayerListDialog.payerSelectListener
                playerListDialog?.dismiss()
            }
        }
        bindingView.title = title
        bindingView.userList = playerList
        playerListDialog = AlertDialog.Builder(windowContext)
            .setView(bindingView.root)
            .show()
        playerListDialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    inline fun show(func: PlayerListDialog.() -> Unit): PlayerListDialog {
        this.func()
        this.show()
        return this
    }
}