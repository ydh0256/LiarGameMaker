package com.duckkite.android.liargamemaker.data.source.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.duckkite.android.liargamemaker.data.model.GameRoom
import com.duckkite.android.liargamemaker.util.getOrAwaitValue
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class GameRoomDaoTest {
    private lateinit var database: GameRoomDatabase

    @get:Rule
    var coroutineRule = InstantTaskExecutorRule()

    @Before
    fun initDb() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            GameRoomDatabase::class.java
        ).build()
    }

    @After
    fun closeDb() = database.close()

    @Test
    fun insertGameRoomDataAndGetById() = runBlocking {
        database.gameRoomDao().insertOfflineGame(DEFAULT_GAME_ROOM_DATA)

        val gameRoom = database.gameRoomDao().getOfflineGameRooms().getOrAwaitValue().getOrNull(0)

        assertThat<GameRoom>(gameRoom as GameRoom, CoreMatchers.notNullValue())
        assertEquals(gameRoom.roomId, DEFAULT_GAME_ROOM_ID)
        assertEquals(gameRoom.roomName, DEFAULT_GAME_ROOM_NAME)
    }

    @Test
    fun insertGameRoomDataReplaceOnConflict() = runBlocking {
        database.gameRoomDao().insertOfflineGame(DEFAULT_GAME_ROOM_DATA)

        GameRoom(roomId = DEFAULT_GAME_ROOM_ID, roomName = "REPLACE").also { replaceData ->
            database.gameRoomDao().insertOfflineGame(replaceData)
        }

        val gameRoomList = database.gameRoomDao().getOfflineGameRooms().getOrAwaitValue()
        val gameRoom = gameRoomList.getOrNull(0)

        assertThat<GameRoom>(gameRoom as GameRoom, CoreMatchers.notNullValue())
        assertEquals(gameRoomList.size, 1)
        assertEquals(gameRoom.roomId, DEFAULT_GAME_ROOM_ID)
        assertNotEquals(gameRoom.roomName, DEFAULT_GAME_ROOM_NAME)
    }

    @Test
    fun insertMultipleGameRoomData() = runBlocking {
        val size = 5
        (0 until size).forEach { i ->
            GameRoom(i.toString()).also { data ->
                database.gameRoomDao().insertOfflineGame(data)
            }
        }

        val gameRoomList = database.gameRoomDao().getOfflineGameRooms().getOrAwaitValue()
        assertEquals(gameRoomList.size, size)
    }

    companion object {
        private const val DEFAULT_GAME_ROOM_ID = "00000001"
        private const val DEFAULT_GAME_ROOM_NAME = "TEST_ROOM"
        private val DEFAULT_GAME_ROOM_DATA = GameRoom(roomId = DEFAULT_GAME_ROOM_ID, roomName = DEFAULT_GAME_ROOM_NAME)
    }
}