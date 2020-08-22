package com.duckkite.android.liargamemaker.ui.main.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.duckkite.android.liargamemaker.R
import com.duckkite.android.liargamemaker.data.model.GameRoom
import com.duckkite.android.liargamemaker.databinding.FragmentMainHomeBinding
import com.duckkite.android.liargamemaker.ui.base.BaseViewModel
import com.duckkite.android.liargamemaker.ui.base.MainActivityFragment
import com.duckkite.android.liargamemaker.ui.game.room.GameRoomActivity
import com.duckkite.android.liargamemaker.ui.main.adapter.GameListAdapter
import com.duckkite.android.liargamemaker.ui.main.adapter.GameListItemActionListener
import kotlinx.android.synthetic.main.fragment_main_home.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class HomeFragment : MainActivityFragment(),
    GameListItemActionListener {

    private val homeViewModel: HomeViewModel by sharedViewModel()
    private lateinit var fragmentHomeBinding: FragmentMainHomeBinding
    override fun getViewModel() = homeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_main_home, container, false)
        fragmentHomeBinding = FragmentMainHomeBinding.bind(root).apply {
            viewModel = homeViewModel
            setLifecycleOwner { this@HomeFragment.lifecycle }
        }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(gameList) {
            adapter = GameListAdapter(this@HomeFragment)
            addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
        }
        showFabButton()
        homeViewModel.fetchOnlineGameList()
    }

    override fun onGameListItemSelect(game: GameRoom) {
        Intent(context, GameRoomActivity::class.java).apply {
            putExtra(GameRoomActivity.INTENT_GAME_ROOM, game)
        }.also {
            startActivity(it)
        }
    }
}