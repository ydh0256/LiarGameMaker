package com.duckkite.android.liargamemaker.data.source.remote

import com.duckkite.android.liargamemaker.data.model.Game
import com.duckkite.android.liargamemaker.data.model.GameMessage
import com.duckkite.android.liargamemaker.data.model.User
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.Query

interface GameRoomDataSource {
    fun fetchGameRoomInformation(roomId: String): DocumentReference
    fun updateMaster(roomId: String, userId: String): Task<Void>
    fun fetchPlayerList(roomId: String): Query
    fun fetchMessageList(roomId: String): Query
    fun sendMessage(roomId: String, message: GameMessage): Task<Void>
    fun addPlayerToChatRoom(roomId: String, user: User): Task<Void>
    fun updatePlayerToChatRoom(roomId: String, user: User): Task<Void>
    fun addGameData(roomId: String, game: Game): Task<Void>
    fun fetchCurrentGame(roomId: String): Query
}