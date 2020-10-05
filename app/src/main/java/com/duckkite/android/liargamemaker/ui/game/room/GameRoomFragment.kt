package com.duckkite.android.liargamemaker.ui.game.room

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.postDelayed
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.LayoutMode
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView
import com.afollestad.materialdialogs.list.listItems
import com.duckkite.android.liargamemaker.R
import com.duckkite.android.liargamemaker.data.model.GameRoom
import com.duckkite.android.liargamemaker.data.model.User
import com.duckkite.android.liargamemaker.databinding.FragmentGameRoomBinding
import com.duckkite.android.liargamemaker.ui.base.BaseFragment
import com.duckkite.android.liargamemaker.ui.game.room.adapter.MessageListAdapter
import com.duckkite.android.liargamemaker.ui.game.room.adapter.PlayerListAdapter
import com.duckkite.android.liargamemaker.ui.game.room.dialog.PlayerListDialog
import kotlinx.android.synthetic.main.fragment_game_room.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class GameRoomFragment : BaseFragment(), GameRoomAction {

    companion object {
        fun newInstance() = GameRoomFragment()
    }

    private val gameRoomViewModel: GameRoomViewModel by sharedViewModel()
    private lateinit var fragmentGameRoomBinding: FragmentGameRoomBinding

    private val messageListAdapter = MessageListAdapter().apply {
        registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                super.onItemRangeInserted(positionStart, itemCount)
                if (!messageList.canScrollVertically(1)) {
                    // 왜 한번에 안내려가는지 모르겠다...ㅠㅠ 두번 나눠 내리면 잘 내려간다
                    messageList.postDelayed(50) {
                        messageList.scrollToPosition(positionStart)
                        messageList.postDelayed(50) {
                            messageList.scrollToPosition(positionStart)
                        }
                    }
                }
            }
        })
    }

    override fun getViewModel() = gameRoomViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val root = inflater.inflate(R.layout.fragment_game_room, container, false)
        fragmentGameRoomBinding = FragmentGameRoomBinding.bind(root).apply {
            viewModel = gameRoomViewModel
            gameRoomAction = this@GameRoomFragment
            lifecycleOwner = this@GameRoomFragment.viewLifecycleOwner
        }
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with((messageList)) {
            adapter = messageListAdapter
            layoutManager = LinearLayoutManager(context).apply {
                stackFromEnd = true
                reverseLayout = true
                setHasFixedSize(true)
            }
            addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->

            }

            addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL).apply {
                ContextCompat.getDrawable(context, R.drawable.div_chat_item)?.let { divDrawable ->
                    setDrawable(divDrawable)
                }
            })
        }
        arguments?.getParcelable<GameRoom>(GameRoomActivity.INTENT_GAME_ROOM)?.let { gameRoom ->
            gameRoomViewModel.fetchGameMessageData(gameRoom.roomId)
        }

        gameRoomViewModel.playerMap.observe(this.viewLifecycleOwner, Observer { userTable ->
            messageListAdapter.userTable = userTable
            messageListAdapter.notifyDataSetChanged()
        })
    }

    override fun onMasterMenuClick() {
        val currentGame = gameRoomViewModel.currentGame.value
        if (gameRoomViewModel.playerList.value?.size ?: 0 < 2) {
            return
        }
        when {
            currentGame == null -> selectStartGameMenu()
            currentGame.currentRound < currentGame.maxRound -> {

            }
            currentGame.currentRound == currentGame.maxRound -> {

            }
        }
    }

    private fun selectStartGameMenu() {
        val activity = activity ?: return
        MaterialDialog(activity).show {
            title(R.string.master_menu_title)
            listItems(items = listOf(getString(R.string.master_menu_start_game),
                getString(R.string.master_menu_change_master))) { _, index, _ ->
                when (index) {
                    0 -> startGame()
                    1 -> changeMaster(activity)
                }
            }
        }
    }

    private fun startGame() {
        val activity = activity ?: return

        val dialog = MaterialDialog(activity, BottomSheet(LayoutMode.WRAP_CONTENT)).show {
            title(R.string.master_menu_make_game)
//            customView(R.layout.custom_view, scrollable = true, horizontalPadding = true)
            positiveButton(R.string.master_menu_make_button) { dialog ->

            }
            negativeButton(android.R.string.cancel)
        }
        dialog.show {  }
    }

    private fun changeMaster(context: Context) {
        val playerList = gameRoomViewModel.playerList.value ?: return
        val masterId = gameRoomViewModel.gameRoom.value?.masterId ?: return
        PlayerListDialog(context).show {
            this.title = context.getString(R.string.master_menu_change_master)
            this.playerList = playerList.filter { user -> user.uuid != masterId }
            this.payerSelectListener = object: PlayerListAdapter.PlayerSelectListener {
                override fun onPlayerSelect(player: User) {
                    gameRoomViewModel.changeMaster(player.uuid)
                }
            }
        }
    }
}
