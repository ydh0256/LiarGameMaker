package com.duckkite.android.liargamemaker.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.firebase.firestore.Exclude
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "game_rooms")
data class GameRoom (
    @PrimaryKey @ColumnInfo(name = "room_id") var roomId: String = "",
    @ColumnInfo(name = "host_id") var hostId: String? = null,
    @ColumnInfo(name = "master_id") var masterId: String? = null,
    @ColumnInfo(name = "room_name") var roomName: String? = null,
    @ColumnInfo(name = "is_private") var isPrivate: Boolean = false,
    @ColumnInfo(name = "private_password") var privatePassword: String? = null,
    @ColumnInfo(name = "generate_time") var generateTime: Long = System.currentTimeMillis(),
    @get:Exclude @set:Exclude @ColumnInfo(name = "last_action_time") var lastActionTime: Long = System.currentTimeMillis(),
    @get:Exclude @set:Exclude @ColumnInfo(name = "user_count") var userCount: Int = 1,
    @Ignore val gameMode: GameMode = GameMode.OFFLINE
) : com.duckkite.android.liargamemaker.data.model.Entity, Parcelable

enum class GameMode {
    ONLINE, OFFLINE
}