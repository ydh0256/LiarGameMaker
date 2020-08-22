package com.duckkite.android.liargamemaker.ui.game.room.dialog

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.duckkite.android.liargamemaker.data.model.User
import com.duckkite.android.liargamemaker.databinding.DialogUserSelectBinding
import com.duckkite.android.liargamemaker.ui.game.room.adapter.PlayerListAdapter

class PlayerListDialog(windowContext: Context) : Dialog(windowContext) {

    private val bindingView: DialogUserSelectBinding
    var title: String? = null
    var playerList: List<User>? = null
    var payerSelectListener: PlayerListAdapter.PlayerSelectListener? = null

    init {
        val layoutInflater = LayoutInflater.from(windowContext)
        bindingView = DialogUserSelectBinding.inflate(layoutInflater, null, false)
    }

    override fun show() {
        with(bindingView.playerList) {
            adapter = PlayerListAdapter().apply {
                payerSelectListener = this@PlayerListDialog.payerSelectListener
            }
            addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
        }
        bindingView.title = title
        bindingView.userList = playerList
        super.show()
    }

    inline fun show(func: PlayerListDialog.() -> Unit): PlayerListDialog {
        this.func()
        this.show()
        return this
    }
}