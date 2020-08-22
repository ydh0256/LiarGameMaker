package com.duckkite.android.liargamemaker.data.source.remote

import androidx.lifecycle.LiveData
import com.duckkite.android.liargamemaker.data.model.GameMode
import com.duckkite.android.liargamemaker.data.model.GameRoom
import com.duckkite.android.liargamemaker.data.source.local.GameRoomDao
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class GameListRepository(
    private val gameRemoteDataSource: FirebaseFirestore,
    private val gameLocalDataSource: GameRoomDao
): GameListDataSource {
    override fun makeNewGameRoom(gameRoom: GameRoom): Task<Void> {
        return gameRemoteDataSource.collection(GAME_LIST_TABLE).document(gameRoom.roomId).set(gameRoom)
    }

    override fun updateGameRoom(gameRoom: GameRoom): Task<Void> {
        return gameRemoteDataSource.collection(GAME_LIST_TABLE).document(gameRoom.roomId).set(gameRoom)
    }

    override fun deleteGameRoom(roomId: String): Task<Void> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun setOfflineGame(gameRoom: GameRoom) {
        gameLocalDataSource.insertOfflineGame(gameRoom)
    }

    override fun fetchOnlineGameList(): Query {
        return gameRemoteDataSource.collection(GAME_LIST_TABLE).whereEqualTo("gameMode", GameMode.OFFLINE.name)
    }

    override fun fetchOfflineGameList(): LiveData<List<GameRoom>> {
        return gameLocalDataSource.getOfflineGameRooms()
    }

    companion object {
        private const val GAME_LIST_TABLE = "GameList"
    }
}