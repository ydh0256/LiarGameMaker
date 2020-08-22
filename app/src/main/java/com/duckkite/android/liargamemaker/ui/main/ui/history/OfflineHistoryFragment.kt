package com.duckkite.android.liargamemaker.ui.main.ui.history

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.duckkite.android.liargamemaker.R
import com.duckkite.android.liargamemaker.data.model.GameRoom
import com.duckkite.android.liargamemaker.databinding.FragmentHistoryBinding
import com.duckkite.android.liargamemaker.ui.base.BaseViewModel
import com.duckkite.android.liargamemaker.ui.base.MainActivityFragment
import com.duckkite.android.liargamemaker.ui.game.room.GameRoomActivity
import com.duckkite.android.liargamemaker.ui.main.adapter.GameListAdapter
import com.duckkite.android.liargamemaker.ui.main.adapter.GameListItemActionListener
import kotlinx.android.synthetic.main.fragment_history.*
import kotlinx.android.synthetic.main.fragment_main_home.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class OfflineHistoryFragment : MainActivityFragment(), GameListItemActionListener {

    private val offlineHistoryViewModel: OfflineHistoryViewModel by sharedViewModel()
    private lateinit var fragmentHistoryBinding: FragmentHistoryBinding
    override fun getViewModel()= offlineHistoryViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_history, container, false)

        fragmentHistoryBinding = FragmentHistoryBinding.bind(root).apply {
            viewModel = offlineHistoryViewModel
            setLifecycleOwner { this@OfflineHistoryFragment.lifecycle }
        }

        showFabButton()
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(historyList) {
            adapter = GameListAdapter(this@OfflineHistoryFragment)
            addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))

        }
        showFabButton()
    }

    override fun onGameListItemSelect(game: GameRoom) {
        Intent(context, GameRoomActivity::class.java).apply {
            putExtra(GameRoomActivity.INTENT_GAME_ROOM, game)
        }.also {
            startActivity(it)
        }
    }
}