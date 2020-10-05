package com.duckkite.android.liargamemaker.data.source.remote

import androidx.lifecycle.LiveData
import com.duckkite.android.liargamemaker.data.model.GameRoom
import com.duckkite.android.liargamemaker.data.model.User
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query

interface GameListDataSource {
    fun makeNewGameRoom(gameRoom: GameRoom): Task<Void>
    fun updateGameRoom(gameRoom: GameRoom): Task<Void>
    suspend fun deleteGameRoom(roomId: String)
    suspend fun setOfflineGame(gameRoom: GameRoom)
    fun fetchOnlineGameList(): Query
    fun fetchOfflineGameList(): LiveData<List<GameRoom>>
}