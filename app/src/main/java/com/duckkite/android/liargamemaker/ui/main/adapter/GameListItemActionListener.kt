package com.duckkite.android.liargamemaker.ui.main.adapter

import com.duckkite.android.liargamemaker.data.model.GameRoom

interface GameListItemActionListener {
    fun onGameListItemSelect(game: GameRoom)
}