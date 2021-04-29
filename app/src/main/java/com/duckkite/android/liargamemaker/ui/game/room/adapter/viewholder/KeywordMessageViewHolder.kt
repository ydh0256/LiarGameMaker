package com.duckkite.android.liargamemaker.ui.game.room.adapter.viewholder

import com.duckkite.android.liargamemaker.data.model.GameMessage
import com.duckkite.android.liargamemaker.databinding.ItemMessageGameBinding
import com.duckkite.android.liargamemaker.util.adapter.BindableViewHolder


class KeywordMessageViewHolder(private val itemMessageGameBinding: ItemMessageGameBinding): BindableViewHolder<GameMessage>(itemMessageGameBinding.root) {
    override fun bindModel(model: GameMessage) {
        with(itemMessageGameBinding) {
            gameMessage = model
        }
    }
}