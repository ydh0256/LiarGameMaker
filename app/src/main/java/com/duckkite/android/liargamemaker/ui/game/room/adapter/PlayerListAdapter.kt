package com.duckkite.android.liargamemaker.ui.game.room.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.duckkite.android.liargamemaker.data.model.User
import com.duckkite.android.liargamemaker.databinding.ItemUserBinding
import com.duckkite.android.liargamemaker.util.adapter.BindableAdapter
import com.duckkite.android.liargamemaker.util.adapter.BindableViewHolder

class PlayerListAdapter(): BindableAdapter<PlayerListAdapter.VH, User>() {
    interface PlayerSelectListener {
        fun onPlayerSelect(player: User)
    }

    var payerSelectListener: PlayerSelectListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val inflater = LayoutInflater.from(parent.context)
        return VH(ItemUserBinding.inflate(inflater, parent, false))
    }

    inner class VH(private val itemUserBinding: ItemUserBinding): BindableViewHolder<User>(itemUserBinding.root) {
        override fun bindModel(model: User) {
            with(itemUserBinding) {
                user = model
                payerSelectListener = this.playerSelectListener
            }
        }
    }
}