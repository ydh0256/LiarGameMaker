package com.duckkite.android.liargamemaker.ui.game.room

import android.os.Bundle
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.duckkite.android.liargamemaker.R
import com.duckkite.android.liargamemaker.data.model.GameMode
import com.duckkite.android.liargamemaker.data.model.GameRoom
import com.duckkite.android.liargamemaker.data.model.InviteInfo
import com.duckkite.android.liargamemaker.databinding.ActivityGameRoomBinding
import com.duckkite.android.liargamemaker.ui.base.BaseActivity
import com.duckkite.android.liargamemaker.ui.base.BaseViewModel
import com.duckkite.android.liargamemaker.ui.game.room.adapter.PlayerListAdapter
import com.duckkite.android.liargamemaker.ui.game.room.dialog.PlayerInviteDialog
import com.duckkite.android.liargamemaker.util.extention.setupActionBar
import kotlinx.android.synthetic.main.activity_game_room.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class GameRoomActivity : BaseActivity() {
    companion object {
        const val INTENT_GAME_ROOM = "INTENT_GAME_ROOM"
    }

    private val gameRoomViewModel: GameRoomViewModel by viewModel()
    private lateinit var gameRoomBinding: ActivityGameRoomBinding
    override fun getViewModel(): BaseViewModel? {
        return null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setDataBinging()
        setActionBar()
        handleIntent(savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.chat_room_menu, menu)
        if (gameRoomViewModel.gameRoom.value?.gameMode == GameMode.ONLINE) {
            menu?.findItem(R.id.chat_menu_add_person)?.isVisible = false
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.chat_menu_add_person -> inviteUser()
            R.id.chat_menu_open -> drawerLayout.openDrawer(GravityCompat.END)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setDataBinging() {
        gameRoomBinding = DataBindingUtil.setContentView(this, R.layout.activity_game_room)
        with(gameRoomBinding) {
            viewModel = gameRoomViewModel
            setLifecycleOwner { this@GameRoomActivity.lifecycle }
        }
    }

    private fun setActionBar() {
        setupActionBar(R.id.toolbar) {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }
    }

    private fun handleIntent(savedInstanceState: Bundle?) {
        intent.getParcelableExtra<GameRoom>(INTENT_GAME_ROOM)?.let { gameRoom ->
            gameRoomViewModel.fetchGameRoomData(gameRoom)
            observeViewModel()
            setPlayerList()
            if (savedInstanceState == null) {
                setFragment(gameRoom)
            }
        } ?: run {
            finish()
        }
    }

    private fun setFragment(gameRoom: GameRoom) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, GameRoomFragment.newInstance().apply {
                arguments = Bundle().apply {
                    putParcelable(INTENT_GAME_ROOM, gameRoom)
                }
            })
            .commitNow()
    }

    private fun observeViewModel() {
        gameRoomViewModel.gameRoom.observe(this, Observer {
            toolbar.title = "${it.roomName}(${gameRoomViewModel.playerList.value?.size ?: 0})"
        })
        gameRoomViewModel.playerList.observe(this, Observer{
            toolbar.title = "${gameRoomViewModel.gameRoom.value?.roomName}(${it.size})"
        })
    }

    private fun setPlayerList() {
        with(playerList) {
            adapter = PlayerListAdapter()
            addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
        }
    }

    private fun inviteUser() {
        PlayerInviteDialog(this).show {
            gameRoomViewModel.gameRoom.value?.let { gameRoom ->
                inviteInfo = InviteInfo(
                    gameRoom.roomId,
                    gameRoom.hostId ?: ""
                )
            }
        }
    }
}
