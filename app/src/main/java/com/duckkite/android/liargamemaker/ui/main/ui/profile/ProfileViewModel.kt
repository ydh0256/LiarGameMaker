package com.duckkite.android.liargamemaker.ui.main.ui.profile

import androidx.lifecycle.MutableLiveData
import com.duckkite.android.liargamemaker.data.event.ErrorEvent
import com.duckkite.android.liargamemaker.data.event.ErrorEventType
import com.duckkite.android.liargamemaker.data.event.ErrorEventViewType
import com.duckkite.android.liargamemaker.data.model.User
import com.duckkite.android.liargamemaker.data.source.remote.UserDataSource
import com.duckkite.android.liargamemaker.ui.base.BaseViewModel
import com.google.firebase.auth.FirebaseUser

class ProfileViewModel(
    private val userDataSource: UserDataSource
) : BaseViewModel() {

    val myProfile = MutableLiveData<User>()

    fun getMyProfile(myAccount: FirebaseUser) {
        userDataSource.getUser(myAccount.uid).addOnSuccessListener { documentSnapShot ->
            myProfile.value = documentSnapShot.toObject(User::class.java)
        }.addOnFailureListener {
            ErrorEvent(
                ErrorEventType.NORMAL,
                ErrorEventViewType.TOAST,
                it.localizedMessage
            ).also { errorEvent ->
                sendErrorEvent(errorEvent)
            }
        }
    }
}