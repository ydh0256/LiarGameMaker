package com.duckkite.android.liargamemaker.util.bind

import android.annotation.SuppressLint
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.duckkite.android.liargamemaker.R
import com.duckkite.android.liargamemaker.data.global.MyProfile
import com.duckkite.android.liargamemaker.data.model.*
import com.duckkite.android.liargamemaker.util.adapter.BindableAdapter
import de.hdodenhof.circleimageview.CircleImageView
import kotlin.math.absoluteValue

@BindingAdapter("items")
fun bindRecyclerView(recyclerView: RecyclerView, items: List<Entity>?) {
    items ?: return
    (recyclerView.adapter as? BindableAdapter<*, Entity>)?.setItems(items)
}

@BindingAdapter("userImage")
fun bindProfileImage(imageView: ImageView, user: User?) {
    user ?: return
    val profileBgList = imageView.context.resources.getIntArray(R.array.profileBg)
    imageView.setImageResource(R.drawable.ic_person_alpha_bg_24dp)
    val index = user.uuid.hashCode().absoluteValue % profileBgList.size
    if (imageView is CircleImageView) {
        imageView.circleBackgroundColor = profileBgList[index]
    } else {
        imageView.setBackgroundColor(profileBgList[index])
    }

    user.photoUrl?.let { photoUrl ->
        Glide.with(imageView.context)
            .load(photoUrl)
            .placeholder(R.drawable.ic_person_alpha_bg_24dp)
            .into(imageView);
    }
}

@BindingAdapter("setNotification")
fun bindNotificationText(textView: TextView, message: GameMessage) {
    when(message.messageContent.messageContentType) {
        MessageContentType.NORMAL -> textView.text = message.messageContent.text
        MessageContentType.ENTERED -> textView.text = textView.context.getString(R.string.notification_user_entered, message.sender.name)
        MessageContentType.LEAVE -> textView.text = textView.context.getString(R.string.notification_user_leave, message.sender.name)
    }
}

@SuppressLint("SetTextI18n")
@BindingAdapter("setGame")
fun bindGameText(textView: TextView, message: GameMessage) {
    val context = textView.context

    if (message.gameContent.playerList.find { user -> MyProfile.isMe(user.uuid) } == null) {
        textView.text = """
            ${context.getString(R.string.game_text_non_member)}
            ${context.getString(R.string.game_text_category)} : ${message.gameContent.category}
            ${context.getString(R.string.game_text_keyword)} : ${message.gameContent.keyWord}
            """.trimIndent()
        return
    }

    if (message.gameContent.liarList.find { user -> MyProfile.isMe(user.uuid) } == null) {
        textView.text = """
            ${context.getString(R.string.game_text_normal)}
            ${context.getString(R.string.game_text_category)} : ${message.gameContent.category}
            ${context.getString(R.string.game_text_keyword)} : ${message.gameContent.keyWord}
            """.trimIndent()
    } else {
        textView.text = """
            ${context.getString(R.string.game_text_liar)}
            ${context.getString(R.string.game_text_category)} : ${message.gameContent.category}
            """.trimIndent()
    }
}