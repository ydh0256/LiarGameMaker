package com.duckkite.android.liargamemaker.data.source.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.duckkite.android.liargamemaker.data.model.GameRoom

@Database(entities = [GameRoom::class], version = 1)
abstract class GameRoomDatabase : RoomDatabase() {

    abstract fun gameRoomDao(): GameRoomDao

    companion object {

        private var INSTANCE: GameRoomDatabase? = null

        private val lock = Any()

        fun getInstance(context: Context): GameRoomDatabase {
            synchronized(lock) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        GameRoomDatabase::class.java, "GameRooms.db"
                    ).build()
                }

                return INSTANCE!!
            }
        }
    }
}