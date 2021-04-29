package com.duckkite.android.liargamemaker.ui.game.room.adapter.viewholder

import com.duckkite.android.liargamemaker.data.model.GameMessage
import com.duckkite.android.liargamemaker.databinding.ItemMessageNotificationBinding
import com.duckkite.android.liargamemaker.util.adapter.BindableViewHolder


class NotificationMessageViewHolder(private val itemMessageNotificationBinding: ItemMessageNotificationBinding): BindableViewHolder<GameMessage>(itemMessageNotificationBinding.root) {
    override fun bindModel(model: GameMessage) {
        with(itemMessageNotificationBinding) {
            gameMessage = model
        }
    }
}