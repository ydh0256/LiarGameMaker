package com.duckkite.android.liargamemaker.ui.game.room

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import com.duckkite.android.liargamemaker.data.event.ErrorEvent
import com.duckkite.android.liargamemaker.data.event.ErrorEventType
import com.duckkite.android.liargamemaker.data.event.ErrorEventViewType
import com.duckkite.android.liargamemaker.data.global.MyProfile
import com.duckkite.android.liargamemaker.data.model.*
import com.duckkite.android.liargamemaker.data.source.remote.GameListDataSource
import com.duckkite.android.liargamemaker.data.source.remote.GameRoomDataSource
import com.duckkite.android.liargamemaker.ui.base.BaseViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*

class GameRoomViewModel(
    private val gameListDataSource: GameListDataSource,
    private val gameRoomDataSource: GameRoomDataSource
) : BaseViewModel() {
    val gameRoom = MutableLiveData<GameRoom>()
    private val _playerList = MutableLiveData<List<User>>().apply { value = emptyList() }
    val playerList: LiveData<List<User>>
        get() = _playerList
    private val _messageList = MutableLiveData<List<GameMessage>>().apply { value = emptyList() }
    val messageList: LiveData<List<GameMessage>>
        get() = _messageList
    val sendMessageText = MutableLiveData<String>()

    val playerMap: LiveData<Map<String, User>> = Transformations.map(playerList) { playerList ->
        playerList.map { player ->
            player
        }.associateBy { it.uuid }
    }

    val isMaster: LiveData<Boolean> = Transformations.map(gameRoom) { gameRoom ->
        MyProfile.isMe(gameRoom.masterId)
    }

    private var isCheckedMyProfileStatus = false

    fun fetchGameRoomData(game: GameRoom) {
        gameRoom.value = game
        gameRoomDataSource.fetchGameRoomInformation(game.roomId)
            .addSnapshotListener { snapshot, error ->
                error?.let {
                    ErrorEvent(ErrorEventType.CLOSE, ErrorEventViewType.TOAST, it.localizedMessage).also { errorEvent ->
                        sendErrorEvent(errorEvent)
                    }
                    return@addSnapshotListener
                }
                snapshot?.let {
                    it.toObject(GameRoom::class.java)?.let { gameRoom ->
                        this.gameRoom.value = gameRoom
                        this.fetchPlayerList(game.roomId)
                        if (gameRoom.gameMode == GameMode.OFFLINE) {
                            viewModelScope.launch {
                                gameListDataSource.setOfflineGame(gameRoom)
                            }
                        }
                    } ?: run {
                        ErrorEvent(ErrorEventType.CUSTOM, ErrorEventViewType.TOAST, customError = ERROR_ROOM_DELETED).also { errorEvent ->
                            sendErrorEvent(errorEvent)
                        }
                    }
                }
            }
    }

    fun fetchGameMessageData(roomId: String) {
        gameRoomDataSource.fetchMessageList(roomId)
            .addSnapshotListener { snapshot, error ->
                error?.let {
                    ErrorEvent(ErrorEventType.CLOSE, ErrorEventViewType.TOAST, it.localizedMessage).also { errorEvent ->
                        sendErrorEvent(errorEvent)
                    }
                    return@addSnapshotListener
                }
                snapshot?.let {
                    val list = it.toObjects(GameMessage::class.java)
                    Timber.d(list.size.toString())
                    Timber.d(it.toString())
                    _messageList.value = list
                }
            }

    }

    fun sendTextMessage() {
        val profile = MyProfile.profile ?: return
        val roomId = gameRoom.value?.roomId ?: return
        val gameMessage = GameMessage(
            UUID.randomUUID().toString(),
            MessageType.CHAT,
            profile,
            MessageContent(text = sendMessageText.value)
        )
        sendMessageText.value = ""
        gameRoomDataSource.sendMessage(roomId, gameMessage).addOnFailureListener { exception ->
            ErrorEvent(ErrorEventType.NORMAL, ErrorEventViewType.TOAST, exception.localizedMessage).also { errorEvent ->
                sendErrorEvent(errorEvent)
            }
        }
    }

    private fun fetchPlayerList(roomId: String) {
        gameRoomDataSource.fetchPlayerList(roomId)
            .addSnapshotListener { snapshot, error ->
                error?.let {
                    ErrorEvent(ErrorEventType.CLOSE, ErrorEventViewType.TOAST, it.localizedMessage).also { errorEvent ->
                        sendErrorEvent(errorEvent)
                    }
                    return@addSnapshotListener
                }
                snapshot?.let {
                    _playerList.value = it.toObjects(User::class.java)
                    when (isCheckedMyProfileStatus) {
                        false -> addOrUpdateMyProfile(roomId)
                    }
                }
            }
    }

    private fun addOrUpdateMyProfile(roomId: String) {
        isCheckedMyProfileStatus = true
        val myProfile = MyProfile.profile ?: return

        _playerList.value?.find { user ->
            MyProfile.isMe(user.uuid)
        }?.let { findUser ->
            if (findUser.photoUrl != myProfile.photoUrl || findUser.name != myProfile.name) {
                gameRoomDataSource.updatePlayerToChatRoom(roomId, findUser)
            }
        } ?: run {
            val profile = MyProfile.profile ?: return
            gameRoomDataSource.addPlayerToChatRoom(roomId, profile)
            sendEnteredMessage()
        }
    }

    private fun sendEnteredMessage() {
        val profile = MyProfile.profile ?: return
        val roomId = gameRoom.value?.roomId ?: return
        val gameMessage = GameMessage(
            UUID.randomUUID().toString(),
            MessageType.NOTIFICATION,
            profile,
            MessageContent(MessageContentType.ENTERED)
        )
        gameRoomDataSource.sendMessage(roomId, gameMessage)
    }

    companion object {
        const val ERROR_ROOM_DELETED = "ERROR_ROOM_DELETED"
    }
}
