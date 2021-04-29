package com.duckkite.android.liargamemaker.data.model


data class Game(
    val category: String = "",
    val keyWord: String = "",
    val playerList: List<User> = arrayListOf(),
    val liarList: List<User> = arrayListOf(),
)

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
        liarList = playerList.shuffled().take(liarCount)
    )
}