package com.duckkite.android.liargamemaker.ui.game.room.adapter.viewholder

import com.duckkite.android.liargamemaker.data.global.MyProfile
import com.duckkite.android.liargamemaker.data.model.GameMessage
import com.duckkite.android.liargamemaker.databinding.ItemMessageChatBinding
import com.duckkite.android.liargamemaker.util.adapter.BindableViewHolder


class ChatMessageViewHolder(private val itemMessageChatBinding: ItemMessageChatBinding): BindableViewHolder<GameMessage>(itemMessageChatBinding.root) {
    override fun bindModel(model: GameMessage) {
        with(itemMessageChatBinding) {
            gameMessage = model
            isMyMessage = MyProfile.isMe(model.sender.uuid)
        }
    }
}