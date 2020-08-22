package com.duckkite.android.liargamemaker.ui.main.ui.profile

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.duckkite.android.liargamemaker.R
import com.duckkite.android.liargamemaker.data.model.User
import com.duckkite.android.liargamemaker.databinding.FragmentProfileBinding
import com.duckkite.android.liargamemaker.ui.base.BaseViewModel
import com.duckkite.android.liargamemaker.ui.base.MainActivityFragment
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_profile.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class ProfileFragment : MainActivityFragment() {

    private val profileViewModel: ProfileViewModel by sharedViewModel()
    private lateinit var profileDataBinding: FragmentProfileBinding
    override fun getViewModel() = profileViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_profile, container, false)
        profileDataBinding = FragmentProfileBinding.bind(root).apply {
            viewModel = profileViewModel
            setLifecycleOwner { this@ProfileFragment.lifecycle }
        }
        hideFabButton()

        profileDataBinding.profileEditButton.setOnClickListener {
            Intent(context, ProfileEditActivity::class.java).also {
                startActivityForResult(it, RC_EDIT_PROFILE)
            }
        }
        profileViewModel.myProfile.observe(this.viewLifecycleOwner, Observer<User> { user ->
            profileHistory.text = user.userHistory.toString()
        })
        return root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == RC_EDIT_PROFILE && resultCode == Activity.RESULT_OK) {
            refreshProfile()
        }
    }

    private fun refreshProfile() {
        FirebaseAuth.getInstance().currentUser?.let {
            profileViewModel.getMyProfile(it)
        }
    }

    companion object {
        private const val RC_EDIT_PROFILE = 100
    }
}