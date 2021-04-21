package com.duckkite.android.liargamemaker.data.model

import java.util.*

data class Game(
    val gameId: String = UUID.randomUUID().toString(),
    val gameStatus: GameStatus = GameStatus.IDLE,
    val category: String,
    val keyWord: String,
    val playerList: List<User>,
    val liarList: List<User>,
    val maxRound: Int,
    val currentRound: Int = 0,
    val voteResult: MutableList<Map<User, User>> = mutableListOf(),
    val startTime: Long = System.currentTimeMillis()
)

enum class GameStatus {
    IDLE, VOTING, FINISH
}

fun makeGame(category: String, keyWord: String, liarCount: Int, masterId: String, isAutoKeyword: Boolean, userList: List<User>): Game {
    val playerList = if (isAutoKeyword) {
        userList
    } else {
        userList.filter { user -> user.uuid != masterId }
    }

    return Game(
        category = category,
        keyWord = keyWord,
        playerList = playerList,
        liarList = playerList.shuffled().take(liarCount),
        maxRound = liarCount
    )
}