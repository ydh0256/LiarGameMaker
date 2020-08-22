package com.duckkite.android.liargamemaker.ui.game.generate

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.duckkite.android.liargamemaker.data.event.*
import com.duckkite.android.liargamemaker.data.global.MyProfile
import com.duckkite.android.liargamemaker.data.model.GameMode
import com.duckkite.android.liargamemaker.data.model.GameRoom
import com.duckkite.android.liargamemaker.data.source.remote.GameListDataSource
import com.duckkite.android.liargamemaker.ui.base.BaseViewModel
import kotlinx.coroutines.launch
import java.util.*

class GameGenerateViewModel(
    private val gameListDataSource: GameListDataSource
) : BaseViewModel() {
    val gameMode = MutableLiveData<GameMode>().apply {
        value = GameMode.ONLINE
    }
    val gameRoomName = MutableLiveData<String>()
    val gameRoomPassword = MutableLiveData<String>()
    val private = MutableLiveData<Boolean>().apply {
        value = false
    }

    fun setGameMode(gameMode: GameMode) {
        this.gameMode.value = gameMode
    }

    fun generateGame() {
        if (gameMode.value == GameMode.ONLINE) {
            ErrorEvent(ErrorEventType.NORMAL, ErrorEventViewType.TOAST, "준비중").also {
                sendErrorEvent(it)
            }
            return
        }

        val gameRoom = GameRoom(
            "${MyProfile.profile?.uuid}@${UUID.randomUUID()}",
            MyProfile.profile?.uuid,
            MyProfile.profile?.uuid,
            gameRoomName.value,
            private.value ?: false,
            gameRoomPassword.value,
            gameMode = gameMode.value ?: GameMode.OFFLINE
        )
        loadStart()
        gameListDataSource.makeNewGameRoom(gameRoom).addOnSuccessListener {
            loadEnd()
            if (gameMode.value == GameMode.OFFLINE) {
                viewModelScope.launch {
                    gameListDataSource.setOfflineGame(gameRoom)
                    moveToGeneratedGameRoom(gameRoom)
                }
            } else {
                moveToGeneratedGameRoom(gameRoom)
            }
        }.addOnFailureListener { exception ->
            ErrorEvent(
                ErrorEventType.NORMAL,
                ErrorEventViewType.TOAST,
                exception.localizedMessage
            ).also { errorEvent ->
                sendErrorEvent(errorEvent)
            }
            loadEnd()
        }
    }

    private fun moveToGeneratedGameRoom(gameRoom: GameRoom) {
        val actionEvent = ActionEvent(ActionEventType.LANDING,
            GameGenerateActivity.LANDING_TO_GAME_ROOM, gameRoom)
        sendActionEvent(actionEvent)
    }
}