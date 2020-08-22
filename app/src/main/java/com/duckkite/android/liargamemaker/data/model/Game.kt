package com.duckkite.android.liargamemaker.data.model

import java.util.*

data class Game(
    val gameId: String = UUID.randomUUID().toString(),
    val category: String,
    val keyWord: String,
    val playerList: List<User>,
    val liarList: List<User>,
    val maxRound: Int,
    val currentRound: Int,
    val voteResult: List<Map<User, User>>,
    val startTime: Long = System.currentTimeMillis()
)