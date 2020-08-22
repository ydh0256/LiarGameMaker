package com.duckkite.android.liargamemaker.data.source.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.duckkite.android.liargamemaker.data.model.GameRoom

@Dao interface GameRoomDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOfflineGame(gameRoom: GameRoom)
    @Query("SELECT * FROM game_rooms")
    fun getOfflineGameRooms(): LiveData<List<GameRoom>>
    @Query("DELETE FROM game_rooms WHERE room_id = :roomId")
    suspend fun deleteOfflineGameRoom(roomId: String)
}