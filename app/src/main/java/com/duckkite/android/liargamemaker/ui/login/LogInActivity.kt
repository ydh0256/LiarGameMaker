package com.duckkite.android.liargamemaker.ui.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.afollestad.materialdialogs.MaterialDialog
import com.duckkite.android.liargamemaker.R
import com.duckkite.android.liargamemaker.data.global.MyProfile
import com.duckkite.android.liargamemaker.data.model.User
import com.duckkite.android.liargamemaker.data.source.remote.UserDataSource
import com.duckkite.android.liargamemaker.ui.main.MainActivity
import com.duckkite.android.liargamemaker.ui.main.ui.profile.ProfileEditActivity
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import org.koin.android.ext.android.inject

class LogInActivity : AppCompatActivity() {

    private val userRepository: UserDataSource by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        moveToNextIfHasFirebaseUser()

        signInButton.setOnClickListener {
            moveToSignIn()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)
            if (resultCode == Activity.RESULT_OK) {
                // Successfully signed in
                moveToNextIfHasFirebaseUser()
            } else {
                response?.error?.localizedMessage?.let {
                    MaterialDialog(this).show {
                        message(text = it)
                    }
                }
            }
        }
    }

    private fun moveToSignIn() {
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.PhoneBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build()
        )

        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build(),
            RC_SIGN_IN
        )
    }

    private fun moveToNextIfHasFirebaseUser() {
        FirebaseAuth.getInstance().currentUser?.let {
            userRepository.getUser(it.uid).addOnSuccessListener { documentSnapshot ->
                documentSnapshot.data?.let {
                    MyProfile.profile = documentSnapshot.toObject(User::class.java)
                    moveToMain()
                } ?: run {
                    moveToProfileInput()
                }
            }.addOnFailureListener { exception ->
                Toast.makeText(this, exception.localizedMessage, Toast.LENGTH_SHORT).show()
                finish()
            }
        } ?: run {
            logInButtons.visibility = View.VISIBLE
        }
    }

    private fun moveToMain() {
        Intent(this, MainActivity::class.java).also { intent ->
            startActivity(intent)
            finish()
        }
    }

    private fun moveToProfileInput() {
        Intent(this, ProfileEditActivity::class.java).apply {
            putExtra(ProfileEditActivity.IS_NEW_USER, true)
        }.also { intent ->
            startActivity(intent)
            finish()
        }
    }

    companion object {
        private const val RC_SIGN_IN = 1000
    }
}
