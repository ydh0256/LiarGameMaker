package com.duckkite.android.liargamemaker.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import androidx.navigation.ui.NavigationUI.setupWithNavController

import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.list.listItems
import com.duckkite.android.liargamemaker.R
import com.duckkite.android.liargamemaker.ui.game.generate.GameGenerateActivity
import com.duckkite.android.liargamemaker.ui.main.ui.home.HomeViewModel
import com.duckkite.android.liargamemaker.ui.main.ui.profile.ProfileViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.zxing.integration.android.IntentIntegrator
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class MainActivity : AppCompatActivity() {
    private val profileViewModel: ProfileViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initNavigation()
        fabStartGame.setOnClickListener {
            startGame()
        }
    }

    override fun onResume() {
        super.onResume()
        getMyProfile()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        IntentIntegrator.parseActivityResult(requestCode, resultCode, data)?.let {
            Timber.d(it.contents)
        } ?: run {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun initNavigation() {
        val navController = findNavController(this, R.id.navHostFragment)
        val appBarConfiguration = AppBarConfiguration.Builder(
            setOf(
                R.id.navigation_home, R.id.navigation_history, R.id.navigation_profile
            )
        ).build()
        setupActionBarWithNavController(this, navController, appBarConfiguration)
        setupWithNavController(navView, navController)
    }

    private fun getMyProfile() {
        FirebaseAuth.getInstance().currentUser?.let {
            profileViewModel.getMyProfile(it)
        }
    }

    private fun startGame() {
        MaterialDialog(this).show {
            title(R.string.main_start_game)
            listItems(items = listOf(getString(R.string.main_start_new_game),
                    getString(R.string.main_start_exit_game))) { _, index, _ ->
                when (index) {
                    0 -> makeNewGame()
                    1 -> joinOfflineGame()
                }
            }
        }
    }

    private fun makeNewGame() {
        Intent(this, GameGenerateActivity::class.java).also {
            startActivity(it)
        }
    }

    private fun joinOfflineGame() {
        IntentIntegrator(this).apply {
            setBeepEnabled(false)
            setOrientationLocked(false)
        }.also {
            it.initiateScan()
        }
    }
}
