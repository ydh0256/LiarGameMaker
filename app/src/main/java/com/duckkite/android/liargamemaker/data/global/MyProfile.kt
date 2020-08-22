package com.duckkite.android.liargamemaker.data.global

import com.duckkite.android.liargamemaker.data.model.User

object MyProfile {
    var profile: User? = null
    fun isMe(userId: String?): Boolean {
        userId ?: return false
        return profile?.uuid == userId
    }
}