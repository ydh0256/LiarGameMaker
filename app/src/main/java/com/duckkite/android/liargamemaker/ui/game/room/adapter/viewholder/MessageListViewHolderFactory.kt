package com.duckkite.android.liargamemaker.ui.game.room.adapter.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import com.duckkite.android.liargamemaker.data.model.GameMessage
import com.duckkite.android.liargamemaker.data.model.MessageType
import com.duckkite.android.liargamemaker.databinding.ItemMessageChatBinding
import com.duckkite.android.liargamemaker.databinding.ItemMessageNotificationBinding
import com.duckkite.android.liargamemaker.util.adapter.BindableViewHolder
import com.duckkite.android.liargamemaker.util.adapter.BindableViewHolderFactory

object MessageListViewHolderFactory: BindableViewHolderFactory<GameMessage> {
    override fun createFromViewType(parent: ViewGroup, viewType: Int): BindableViewHolder<GameMessage> {
        val inflater = LayoutInflater.from(parent.context)
        return when(MessageType.values()[viewType]) {
            MessageType.CHAT -> ChatMessageViewHolder(ItemMessageChatBinding.inflate(inflater, parent, false))
            MessageType.NOTIFICATION  -> NotificationMessageViewHolder(ItemMessageNotificationBinding.inflate(inflater, parent, false))
        }
    }
}