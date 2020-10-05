package com.duckkite.android.liargamemaker.ui.game.generate

import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.duckkite.android.liargamemaker.R
import com.duckkite.android.liargamemaker.data.event.ActionEventType
import com.duckkite.android.liargamemaker.data.model.GameRoom
import com.duckkite.android.liargamemaker.databinding.ActivityGameGenerateBinding
import com.duckkite.android.liargamemaker.ui.base.BaseActivity
import com.duckkite.android.liargamemaker.ui.game.room.GameRoomActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class GameGenerateActivity : BaseActivity() {

    private val gameGenerateViewModel: GameGenerateViewModel by viewModel()
    private lateinit var gameGenerateBinding: ActivityGameGenerateBinding
    override fun getViewModel() = gameGenerateViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setDataBinging()
        addEventHandler()
    }

    private fun setDataBinging() {
        gameGenerateBinding = DataBindingUtil.setContentView(this, R.layout.activity_game_generate)
        with(gameGenerateBinding) {
            viewModel = gameGenerateViewModel
            setLifecycleOwner { this@GameGenerateActivity.lifecycle }
        }
    }

    private fun addEventHandler() {
        gameGenerateViewModel.actionEvent.observe(this, Observer { event ->
            event.getContentIfNotHandled()?.let { actionEvent ->
                if (actionEvent.type == ActionEventType.LANDING) {
                    when(actionEvent.target) {
                        LANDING_TO_GAME_ROOM -> {
                            val game = (actionEvent.entity as? GameRoom) ?: return@Observer
                            Intent(this, GameRoomActivity::class.java).apply {
                                putExtra(GameRoomActivity.INTENT_GAME_ROOM, game)
                            }.also {
                                startActivity(it)
                                finish()
                            }
                        }
                    }
                }
            }
        })
    }

    companion object {
        const val LANDING_TO_GAME_ROOM = "randingToGameRoom"
    }
}
