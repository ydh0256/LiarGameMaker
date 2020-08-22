package com.duckkite.android.liargamemaker.ui.game.room.adapter

import android.view.ViewGroup
import com.duckkite.android.liargamemaker.data.model.GameMessage
import com.duckkite.android.liargamemaker.data.model.User
import com.duckkite.android.liargamemaker.ui.game.room.adapter.viewholder.MessageListViewHolderFactory
import com.duckkite.android.liargamemaker.util.adapter.BindableAdapter
import com.duckkite.android.liargamemaker.util.adapter.BindableViewHolder

class MessageListAdapter: BindableAdapter<BindableViewHolder<GameMessage>, GameMessage>() {
    var userTable: Map<String, User>? = null

    override fun getItemViewType(position: Int) = entityList[position].getType()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindableViewHolder<GameMessage> {
        return MessageListViewHolderFactory.createFromViewType(parent, viewType)
    }

    override fun onBindViewHolder(holder: BindableViewHolder<GameMessage>, position: Int) {
        val key = entityList[position].sender.uuid
        when (userTable?.containsKey(key)) {
            true -> {
                userTable?.getValue(key)?.let { user ->
                    entityList[position].sender = user
                }
            }
        }
        super.onBindViewHolder(holder, position)
    }
}