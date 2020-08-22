package com.duckkite.android.liargamemaker.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserHistory(
    val liarCount: Int = 0,
    val liarWinningCount: Int = 0,
    val normalCount: Int = 0,
    val normalWinningCount: Int = 0,
    val pointOutCount: Int = 0,
    val hitCount: Int = 0
) : Parcelable