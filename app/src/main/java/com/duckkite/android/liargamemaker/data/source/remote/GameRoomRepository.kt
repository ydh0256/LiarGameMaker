package com.duckkite.android.liargamemaker.data.source.remote

import com.duckkite.android.liargamemaker.data.model.Game
import com.duckkite.android.liargamemaker.data.model.GameMessage
import com.duckkite.android.liargamemaker.data.model.User
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class GameRoomRepository(
    private val gameRemoteDataSource: FirebaseFirestore
) : GameRoomDataSource {
    override fun fetchGameRoomInformation(roomId: String): DocumentReference {
        return gameRemoteDataSource.collection(GAME_LIST_TABLE).document(roomId)
    }

    override fun updateMaster(roomId: String, userId: String): Task<Void> {
        return gameRemoteDataSource.collection(GAME_LIST_TABLE).document(roomId).update("masterId", userId)
    }

    override fun fetchPlayerList(roomId: String): Query {
        return gameRemoteDataSource.collection(GAME_LIST_TABLE).document(roomId).collection(PLAYER_LIST)
    }

    override fun fetchMessageList(roomId: String): Query {
        return gameRemoteDataSource.collection(GAME_LIST_TABLE).document(roomId)
            .collection(MESSAGE_LIST).orderBy("messageTime", Query.Direction.DESCENDING)
    }

    override fun sendMessage(roomId: String, message: GameMessage): Task<Void> {
        return gameRemoteDataSource.collection(GAME_LIST_TABLE).document(roomId)
            .collection(MESSAGE_LIST).document().set(message)
    }

    override fun addPlayerToChatRoom(roomId: String, user: User): Task<Void> {
        return gameRemoteDataSource.collection(GAME_LIST_TABLE).document(roomId)
            .collection(PLAYER_LIST).document(user.uuid).set(user)
    }

    override fun updatePlayerToChatRoom(roomId: String, user: User): Task<Void> {
        return gameRemoteDataSource.collection(GAME_LIST_TABLE).document(roomId)
            .collection(PLAYER_LIST).document(user.uuid).set(user)
    }

    companion object {
        private const val GAME_LIST_TABLE = "GameList"
        private const val MESSAGE_LIST = "GameMessages"
        private const val PLAYER_LIST = "Players"
    }
}