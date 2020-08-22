package com.duckkite.android.liargamemaker.ui.main.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.duckkite.android.liargamemaker.data.event.ErrorEvent
import com.duckkite.android.liargamemaker.data.event.ErrorEventType
import com.duckkite.android.liargamemaker.data.event.ErrorEventViewType
import com.duckkite.android.liargamemaker.data.model.GameRoom
import com.duckkite.android.liargamemaker.data.source.remote.GameListDataSource
import com.duckkite.android.liargamemaker.ui.base.BaseViewModel

class HomeViewModel(
    private val gameListDataSource: GameListDataSource
) : BaseViewModel() {
    private val _items = MutableLiveData<List<GameRoom>>().apply { value = emptyList() }
    val items: LiveData<List<GameRoom>>
        get() = _items

    private var isFetched = false

    fun fetchOnlineGameList() {
        if (isFetched) {
            return
        }
        gameListDataSource.fetchOnlineGameList().addSnapshotListener { snapshot, error ->
            error?.let {
                ErrorEvent(ErrorEventType.NORMAL, ErrorEventViewType.TOAST, it.localizedMessage).also { errorEvent ->
                    sendErrorEvent(errorEvent)
                }
                isFetched = false
                return@addSnapshotListener
            }
            snapshot?.let {
                isFetched = true
                _items.value = it.toObjects(GameRoom::class.java)
            }
        }
    }
}