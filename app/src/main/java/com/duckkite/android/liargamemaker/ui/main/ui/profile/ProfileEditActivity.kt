package com.duckkite.android.liargamemaker.ui.main.ui.profile

import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.duckkite.android.liargamemaker.R
import com.duckkite.android.liargamemaker.data.event.ActionEventType
import com.duckkite.android.liargamemaker.databinding.ActivityProfileEditBinding
import com.duckkite.android.liargamemaker.ui.base.BaseActivity
import com.duckkite.android.liargamemaker.ui.main.MainActivity
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.auth.FirebaseAuth
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileEditActivity : BaseActivity() {

    private val profileEditViewModel: ProfileEditViewModel by viewModel()
    private lateinit var profileBinding: ActivityProfileEditBinding
    override fun getViewModel() = profileEditViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setDataBinging()
        getUserInfo()
        addEventHandler()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                RESULT_CODE_IMAGE_SELECT -> {
                    data?.data?.let { uri ->
                        profileEditViewModel.uploadProfileImage(uri)
                    }
                }
            }
        }
    }

    private fun setDataBinging() {
        profileBinding = DataBindingUtil.setContentView(this, R.layout.activity_profile_edit)
        with(profileBinding) {
            viewModel = profileEditViewModel
            setLifecycleOwner { this@ProfileEditActivity.lifecycle }
        }

    }

    private fun getUserInfo() {
        FirebaseAuth.getInstance().currentUser?.let {
            profileEditViewModel.getMyProfile(it, intent.getBooleanExtra(IS_NEW_USER, false))
        } ?: run {
            finish()
        }
    }

    private fun addEventHandler() {
        profileEditViewModel.actionEvent.observe(this, Observer { event ->
            event.getContentIfNotHandled()?.let { actionEvent ->
                when (actionEvent.type) {
                    ActionEventType.LANDING -> {
                        if (actionEvent.target == LANDING_MAIN) {
                            moveToMainActivity()
                        }
                    }
                    ActionEventType.CUSTOM -> {
                        if (actionEvent.target == SELECT_PICTURE) {
                            selectPicture()
                        }
                    }
                    else -> { }
                }
            }
        })
    }

    private fun moveToMainActivity() {
        if (intent.getBooleanExtra(IS_NEW_USER, false)) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        finish()
    }

    private fun selectPicture() {
        ImagePicker.with(this)
            .cropSquare()
            .maxResultSize(256, 256)
            .start(RESULT_CODE_IMAGE_SELECT)
    }

    companion object {
        const val IS_NEW_USER = "IS_NEW_USER"
        const val LANDING_MAIN = "MAIN"
        const val SELECT_PICTURE = "SELECT_PICTURE"
        const val RESULT_CODE_IMAGE_SELECT = 101
    }
}
