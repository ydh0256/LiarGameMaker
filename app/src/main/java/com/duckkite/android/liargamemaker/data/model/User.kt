package com.duckkite.android.liargamemaker.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    val uuid: String = "",
    var name: String? = null,
    val email: String? = null,
    var photoUrl: String? = null,
    val userHistory: UserHistory = UserHistory()
) : Parcelable, Entity