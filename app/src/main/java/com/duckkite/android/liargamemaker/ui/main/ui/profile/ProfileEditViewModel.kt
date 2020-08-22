package com.duckkite.android.liargamemaker.ui.main.ui.profile

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.duckkite.android.liargamemaker.data.event.*
import com.duckkite.android.liargamemaker.data.global.MyProfile
import com.duckkite.android.liargamemaker.data.model.User
import com.duckkite.android.liargamemaker.data.source.remote.UserDataSource
import com.duckkite.android.liargamemaker.ui.base.BaseViewModel
import com.google.firebase.auth.FirebaseUser
import java.io.File

class ProfileEditViewModel(
    private val userDataSource: UserDataSource
) : BaseViewModel() {
    val nickName = MutableLiveData<String>()
    val myProfile = MutableLiveData<User>()
    val isFileUpload = MutableLiveData<Boolean>().apply { value = false }

    fun getMyProfile(myAccount: FirebaseUser, isNewUser: Boolean = false) {
        loadStart()
        if (isNewUser) {
            loadEnd()
            myProfile.value = User(myAccount.uid, myAccount.displayName, myAccount.email, myAccount.photoUrl?.toString()).also {
                nickName.value = it.name
            }
            return
        }

        userDataSource.getUser(myAccount.uid).addOnSuccessListener { documentSnapShot ->
            loadEnd()
            myProfile.value = documentSnapShot.toObject(User::class.java)?.also {
                nickName.value = it.name
            }
        }.addOnFailureListener {
            ErrorEvent(ErrorEventType.CLOSE, ErrorEventViewType.TOAST, it.localizedMessage).also { errorEvent ->
                sendErrorEvent(errorEvent)
            }
        }
    }

    fun saveMyProfile() {
        loadStart()
        myProfile.value?.apply {
            name = nickName.value
        }?.also { user ->
            userDataSource.addMyProfile(user).addOnSuccessListener {
                loadEnd()
                ActionEvent(ActionEventType.LANDING, ProfileEditActivity.LANDING_MAIN).also { actionEvent ->
                    sendActionEvent(actionEvent)
                }
                MyProfile.profile = user
            }.addOnFailureListener { exception ->
                loadEnd()
                ErrorEvent(ErrorEventType.NORMAL, ErrorEventViewType.TOAST, exception.localizedMessage).also { errorEvent ->
                    sendErrorEvent(errorEvent)
                }
            }
        }
    }

    fun selectProfileImage() {
        ActionEvent(ActionEventType.CUSTOM, ProfileEditActivity.SELECT_PICTURE).also { actionEvent ->
            sendActionEvent(actionEvent)
        }
    }

    fun uploadProfileImage(profileImage: Uri) {
        isFileUpload.value = true

        userDataSource.uploadProfileImage(myProfile.value?.uuid ?: "", profileImage).continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }
            task.result?.storage?.downloadUrl
        }.addOnCompleteListener { task ->
            isFileUpload.value = false
            if (task.isSuccessful) {
                val downloadUri = task.result
                myProfile.value?.photoUrl = downloadUri.toString()
                val profile = myProfile.value
                myProfile.value = null
                myProfile.value = profile
            } else {
                ErrorEvent(ErrorEventType.NORMAL, ErrorEventViewType.TOAST, task.exception?.localizedMessage).also { errorEvent ->
                    sendErrorEvent(errorEvent)
                }
            }
        }
    }
}