package com.duckkite.android.liargamemaker.data.model

import com.duckkite.android.liargamemaker.util.adapter.TypeableEntity
import java.util.*

data class GameMessage(
    val messageId: String = UUID.randomUUID().toString(),
    val messageType: MessageType = MessageType.NOTIFICATION,
    var sender: User = User(),
    val messageContent: MessageContent = MessageContent(),
    val messageTime: Long = System.currentTimeMillis()

) : TypeableEntity {
    override fun getType() = messageType.ordinal
}

enum class MessageType {
    NOTIFICATION,
    CHAT
}

data class MessageContent(
    val messageContentType: MessageContentType = MessageContentType.NORMAL,
    val text: String? = null,
    val imageUrl: String? = null
)

enum class MessageContentType {
    NORMAL,
    ENTERED,
    LEAVE,
    NEW_GAME
}

fun makeChatMessage(profile: User, sendMessageText: String?): GameMessage {
    return GameMessage(
        messageType = MessageType.CHAT,
        sender = profile,
        messageContent = MessageContent(text = sendMessageText)
    )
}

fun makeEnterMessage(profile: User): GameMessage {
    return GameMessage(
        messageType = MessageType.NOTIFICATION,
        sender = profile,
        messageContent = MessageContent(MessageContentType.ENTERED)
    )
}
