package com.duckkite.android.liargamemaker.ui.main.ui.history

import androidx.lifecycle.LiveData
import com.duckkite.android.liargamemaker.data.model.GameRoom
import com.duckkite.android.liargamemaker.data.source.remote.GameListDataSource
import com.duckkite.android.liargamemaker.ui.base.BaseViewModel

class OfflineHistoryViewModel(
    gameListDataSource: GameListDataSource
) : BaseViewModel() {
    private val _items = gameListDataSource.fetchOfflineGameList()
    val items: LiveData<List<GameRoom>>
        get() = _items
}