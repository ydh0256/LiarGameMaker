package com.duckkite.android.liargamemaker.ui.game.room

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.postDelayed
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.duckkite.android.liargamemaker.R
import com.duckkite.android.liargamemaker.data.model.GameRoom
import com.duckkite.android.liargamemaker.databinding.FragmentGameRoomBinding
import com.duckkite.android.liargamemaker.ui.base.BaseFragment
import com.duckkite.android.liargamemaker.ui.base.BaseViewModel
import com.duckkite.android.liargamemaker.ui.game.room.adapter.MessageListAdapter
import com.duckkite.android.liargamemaker.util.extention.smoothScrollToEnd
import kotlinx.android.synthetic.main.fragment_game_room.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class GameRoomFragment : BaseFragment() {

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
}
