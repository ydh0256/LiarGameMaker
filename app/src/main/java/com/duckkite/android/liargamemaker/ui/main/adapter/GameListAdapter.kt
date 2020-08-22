package com.duckkite.android.liargamemaker.ui.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.duckkite.android.liargamemaker.data.model.GameRoom
import com.duckkite.android.liargamemaker.databinding.ItemGameListBinding
import com.duckkite.android.liargamemaker.util.adapter.BindableAdapter
import com.duckkite.android.liargamemaker.util.adapter.BindableViewHolder

class GameListAdapter(
    private val gameListItemActionListener: GameListItemActionListener? = null
): BindableAdapter<GameListAdapter.VH, GameRoom>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val inflater = LayoutInflater.from(parent.context)
        return VH(ItemGameListBinding.inflate(inflater, parent, false))
    }

    inner class VH(private val itemGameListBinding: ItemGameListBinding): BindableViewHolder<GameRoom>(itemGameListBinding.root) {
        override fun bindModel(model: GameRoom) {
            with(itemGameListBinding) {
                gameRoom = model
                actionListener = gameListItemActionListener
            }
        }
    }
}